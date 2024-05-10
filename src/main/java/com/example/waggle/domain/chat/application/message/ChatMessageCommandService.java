package com.example.waggle.domain.chat.application.message;

import com.example.waggle.domain.chat.presentation.dto.ChatMessageDto;

public interface ChatMessageCommandService {

    String createChatMessage(ChatMessageDto chatMessageDto);

}
