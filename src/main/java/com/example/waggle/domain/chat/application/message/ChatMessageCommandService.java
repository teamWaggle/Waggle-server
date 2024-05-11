package com.example.waggle.domain.chat.application.message;

import com.example.waggle.domain.chat.presentation.dto.MessageDto;

public interface ChatMessageCommandService {

    String createChatMessage(MessageDto chatMessageDto);

}
