package com.example.waggle.domain.chat.application.message;

import com.example.waggle.domain.chat.persistence.entity.ChatMessage;
import com.example.waggle.domain.chat.persistence.dao.jpa.ChatMessageRepository;
import com.example.waggle.domain.chat.presentation.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatMessageCommandServiceImpl implements ChatMessageCommandService {

    private final ChatMessageRepository chatMessageRepository;

    @Override
    public String createChatMessage(ChatMessageDto chatMessageDto) {
        ChatMessage chatMessage = chatMessageRepository.save(chatMessageDto.toEntity());
        return chatMessage.getId();
    }
}
