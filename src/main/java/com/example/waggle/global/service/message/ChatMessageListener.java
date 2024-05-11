package com.example.waggle.global.service.message;


import com.example.waggle.domain.chat.presentation.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatMessageListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @KafkaListener(topics = "waggle-chat", groupId = "chat-group")
    public void receiveMessage(MessageDto message) {
        messagingTemplate.convertAndSend("/subscribe/" + message.getChatRoomId(), message);
    }
}
