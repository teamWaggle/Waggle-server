package com.example.waggle.domain.chat.service;

import com.example.waggle.web.dto.chat.ChatMessageDto;

public interface ChatMessageCommandService {

    String createChatMessage(ChatMessageDto chatMessageDto);

}
