package com.example.waggle.domain.chat.config;

import com.example.waggle.domain.chat.service.RoomService;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.security.TokenService;
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
    private final RoomService roomService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String username = verifyAccessToken(getAccessToken(accessor));
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
        String chatRoomId = getChatRoomId(accessor);

        // 채팅방에 사용자를 추가하는 처리
        roomService.connectChatRoom(chatRoomId, username);

        // 읽지 않은 채팅을 전부 읽음 처리
        // TODO chatService.updateCountAllZero(chatRoomId, username);

        // 채팅방에 접속중인 사용자에게 메시지를 보내기 위한 로직
        // 예를 들어, 새로운 사용자가 채팅방에 들어왔다는 알림 메시지를 보내거나,
        // 채팅방의 현재 상태를 업데이트하는 등의 처리
        broadcastNewMember(chatRoomId, username);
    }

    private void broadcastNewMember(String chatRoomId, String username) {
        // TODO 채팅방에 있는 모든 사용자에게 새로운 사용자가 들어왔다는 메시지를 전송하는 로직
    }

    private String verifyAccessToken(String accessToken) {
        log.info("accessToken = {}", accessToken);
        if (!tokenService.validateToken(accessToken)) {
            throw new MemberHandler(ErrorStatus.AUTH_INVALID_TOKEN);
        }

        return tokenService.getAuthentication(accessToken).getName();
    }

    private String getChatRoomId(StompHeaderAccessor accessor) {
        return accessor.getFirstNativeHeader("roomId");
    }
}
