package com.example.waggle.web.controller;

import com.example.waggle.domain.chat.entity.ChatMessageDto;
import com.example.waggle.domain.chat.service.ChatMessageCommandService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import java.security.Principal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatMessageController {

    private final SimpMessageSendingOperations sendingOperations;
    private final MemberQueryService memberQueryService;
    private final ChatMessageCommandService chatMessageCommandService;


    @MessageMapping("/message")
    public void sendMessage(Principal principal, @Payload ChatMessageDto message) {
        log.info("principal = {}", principal);
        Member member = memberQueryService.getMemberByUsername(principal.getName());
        processMessage(member, message);
    }

    private void processMessage(Member member, ChatMessageDto message) {
        message.setSendTimeAndSenderInfo(LocalDateTime.now(), member.getNickname(), member.getProfileImgUrl());

        switch (message.getChatMessageType()) {
            case ENTER:
                message.setContent("🐶 " + member.getNickname() + "님이 입장하셨습니다.");
                break;
            case EXIT:
                message.setContent("👋 " + member.getNickname() + "님이 퇴장하셨습니다.");
                break;
            case TALK:
                break;
        }
        chatMessageCommandService.createChatMessage(message);
        sendingOperations.convertAndSend("/subscribe/" + message.getChatRoomId(), message);
    }

}
