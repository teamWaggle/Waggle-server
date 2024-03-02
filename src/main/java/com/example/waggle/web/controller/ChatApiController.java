package com.example.waggle.web.controller;

import com.example.waggle.domain.chat.dto.Message;
import com.example.waggle.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatApiController {

    private final ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/subscribe/waggle-chat")
    public void sendMessage(@Payload Message message) {
        chatService.sendMessage(message, message.getSenderUsername());
    }

}
