package com.example.waggle.domain.chat.config;

import com.example.waggle.domain.chat.service.ChatRoomService;
import com.example.waggle.domain.chat.service.ChatService;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.security.TokenService;
import java.util.Objects;
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
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // StompCommand에 따라서 로직을 분기해서 처리하는 메서드를 호출한다.
        String username = verifyAccessToken(getAccessToken(accessor));
        log.info("StompAccessor = {}", accessor);
        handleMessage(accessor.getCommand(), accessor, username);
        return message;
    }

    private void handleMessage(StompCommand stompCommand, StompHeaderAccessor accessor, String username) {
        switch (stompCommand) {
            case CONNECT:
                connectToChatRoom(accessor, username);
                break;
            case SUBSCRIBE:
            case SEND:
                break;
        }
    }

    private String getAccessToken(StompHeaderAccessor accessor) {
        return accessor.getFirstNativeHeader("Authorization");
    }

    private void connectToChatRoom(StompHeaderAccessor accessor, String username) {
        // 채팅방 번호를 가져온다.
        Integer chatRoomNo = getChatRoomNo(accessor);

        // 채팅방 입장 처리 -> Redis에 입장 내역 저장
        chatRoomService.connectChatRoom(chatRoomNo, username);
        // 읽지 않은 채팅을 전부 읽음 처리
        chatService.updateCountAllZero(chatRoomNo, username);
        // 현재 채팅방에 접속중인 인원이 있는지 확인한다.
        boolean isConnected = chatRoomService.isConnected(chatRoomNo);

        if (isConnected) {
            chatService.updateMessage(username, chatRoomNo);
        }
    }

    private String verifyAccessToken(String accessToken) {
        if (!tokenService.validateToken(accessToken)) {
            throw new MemberHandler(ErrorStatus.AUTH_INVALID_TOKEN);
        }

        return tokenService.getAuthentication(accessToken).getName();
    }

    private Integer getChatRoomNo(StompHeaderAccessor accessor) {
        return
                Integer.valueOf(
                        Objects.requireNonNull(
                                accessor.getFirstNativeHeader("chatRoomId")
                        ));
    }
}
