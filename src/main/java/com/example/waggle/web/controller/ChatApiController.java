package com.example.waggle.web.controller;

import com.example.waggle.domain.chat.dto.Message;
import com.example.waggle.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatApiController {

    private final ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Message message) {
        chatService.sendMessage(message, message.getSenderUsername());
    }

    @MessageMapping("/chat.newMessage")
    @SendTo("/topic/public")
    public Message sendNotification(@Payload Message message) {
        return chatService.sendNotificationAndSaveMessage(message);
    }

}
