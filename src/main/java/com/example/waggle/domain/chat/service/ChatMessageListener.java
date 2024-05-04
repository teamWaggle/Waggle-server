package com.example.waggle.domain.chat.service;


import com.example.waggle.web.dto.chat.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatMessageListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @KafkaListener(topics = "waggle-chat", groupId = "chat-group")
    public void receiveMessage(ChatMessageDto message) {
        messagingTemplate.convertAndSend("/subscribe/" + message.getChatRoomId(), message);
    }
}
