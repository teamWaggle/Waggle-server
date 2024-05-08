package com.example.waggle.global.config.websocket;

import com.example.waggle.domain.member.application.MemberQueryService;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.exception.object.handler.MemberHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.security.jwt.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import static org.springframework.messaging.simp.stomp.StompCommand.*;

@Order(Ordered.HIGHEST_PRECEDENCE + 99) // 우선 순위를 높게 설정해서 SecurityFilter들 보다 앞서 실행되게 해준다.
@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final TokenService tokenService;
    private final MemberQueryService memberQueryService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor.getCommand() == CONNECT || accessor.getCommand() == SEND || accessor.getCommand() == SUBSCRIBE) {
            Member member = getMemberByAccessToken(getAccessToken(accessor));
            accessor.setUser(() -> member.getUsername());
        }
        return message;
    }

    private String getAccessToken(StompHeaderAccessor accessor) {
        String token = accessor.getFirstNativeHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7).trim();
        }
        throw new MemberHandler(ErrorStatus.AUTH_IS_NULL);
    }

    private Member getMemberByAccessToken(String accessToken) {
        if (!tokenService.validateToken(accessToken)) {
            throw new MemberHandler(ErrorStatus.AUTH_INVALID_TOKEN);
        }
        String username = tokenService.getAuthentication(accessToken).getName();
        return memberQueryService.getMemberByUsername(username);
    }
}