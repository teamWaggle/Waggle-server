package com.example.waggle.domain.chat.presentation.controller;

import com.example.waggle.domain.chat.application.message.ChatMessageCommandService;
import com.example.waggle.domain.chat.presentation.dto.MessageDto;
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
    private final KafkaTemplate<String, MessageDto> kafkaTemplate;
    private final String KAFKA_TOPIC = "waggle-chat";

    @MessageMapping("/message")
    public void sendMessage(Principal principal, @Payload MessageDto message) {
        log.info("principal = {}", principal);
        Member member = memberQueryService.getMemberByUsername(principal.getName());
        processMessage(member, message);
    }

    private void processMessage(Member member, MessageDto message) {
        message.setSendTimeAndSenderInfo(LocalDateTime.now(), member.getNickname(), member.getProfileImgUrl());

        switch (message.getChatMessageType()) {
            case JOIN:
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
