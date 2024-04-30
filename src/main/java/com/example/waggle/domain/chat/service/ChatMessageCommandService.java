package com.example.waggle.domain.chat.service;

import com.example.waggle.domain.chat.entity.ChatMessageDto;

public interface ChatMessageCommandService {

    String createChatMessage(ChatMessageDto chatMessageDto);

}
