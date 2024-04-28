package com.example.waggle.domain.chat.service;

import com.example.waggle.domain.chat.dto.MessageDto;

public interface ChatMessageCommandService {

    String createChatMessage(MessageDto messageDto);

}
