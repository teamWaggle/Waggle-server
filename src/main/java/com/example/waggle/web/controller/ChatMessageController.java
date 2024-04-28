package com.example.waggle.web.controller;

import com.example.waggle.domain.chat.dto.MessageDto;
import com.example.waggle.domain.chat.dto.MessageDto.MessageType;
import com.example.waggle.domain.chat.service.ChatRoomCommandService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatMessageController {

    private final SimpMessageSendingOperations sendingOperations;
    private final MemberQueryService memberQueryService;
    private final ChatRoomCommandService chatRoomCommandService;


    @MessageMapping("/message")
    public void enter(MessageDto message) {
        Member member = memberQueryService.getMemberByUserUrl(message.getSenderUserUrl());
        LocalDateTime accessTime = LocalDateTime.now();
        chatRoomCommandService.updateLastAccessTime(member, message.getChatRoomId(), accessTime);
        message.setSendTimeAndSenderInfo(LocalDateTime.now(), member.getNickname(), member.getProfileImgUrl());
        if (message.getMessageType().equals(MessageType.ENTER)) {
            message.setContent("🐶 " + message.getSenderNickname() + "님이 입장하셨습니다.");
        }
        sendingOperations.convertAndSend("/subscribe/" + message.getChatRoomId(), message);
    }
}
