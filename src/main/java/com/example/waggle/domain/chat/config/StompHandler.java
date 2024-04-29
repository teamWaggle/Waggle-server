package com.example.waggle.domain.chat.config;

import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE + 99) // 우선 순위를 높게 설정해서 SecurityFilter들 보다 앞서 실행되게 해준다.
@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final TokenService tokenService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        try {
            String username = verifyAccessToken(getAccessToken(accessor));
            handleMessage(accessor.getCommand(), accessor, username);
        } catch (Exception e) {
            log.error("Error handling message: ", e);
        }
        return message;
    }

    private void handleMessage(StompCommand stompCommand, StompHeaderAccessor accessor, String username) {
        switch (stompCommand) {
            case CONNECT:
                break;
            case SUBSCRIBE:
                break;
            case SEND:
                break;
            case DISCONNECT:
                break;
        }
    }

    private String getAccessToken(StompHeaderAccessor accessor) {
        String token = accessor.getFirstNativeHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7).trim();
        }
        return null;
    }

    private String verifyAccessToken(String accessToken) {
        if (!tokenService.validateToken(accessToken)) {
            throw new MemberHandler(ErrorStatus.AUTH_INVALID_TOKEN);
        }
        return tokenService.getAuthentication(accessToken).getName();
    }
}