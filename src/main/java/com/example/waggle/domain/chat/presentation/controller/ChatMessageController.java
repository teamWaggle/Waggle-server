package com.example.waggle.domain.chat.presentation.controller;

import com.example.waggle.domain.chat.application.chatMessage.ChatMessageCommandService;
import com.example.waggle.domain.chat.presentation.dto.ChatMessageDto;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.member.application.MemberQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatMessageController {

    private final SimpMessageSendingOperations sendingOperations;
    private final MemberQueryService memberQueryService;
    private final ChatMessageCommandService chatMessageCommandService;
    private final KafkaTemplate<String, ChatMessageDto> kafkaTemplate;
    private final String KAFKA_TOPIC = "waggle-chat";

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

        kafkaTemplate.send(KAFKA_TOPIC, message);
        chatMessageCommandService.createChatMessage(message);
    }

}
