package com.example.waggle.domain.chat.service;

import com.example.waggle.domain.chat.dto.MessageDto;
import com.example.waggle.domain.chat.entity.ChatMessage;
import com.example.waggle.domain.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatMessageCommandServiceImpl implements ChatMessageCommandService {

    private final ChatMessageRepository chatMessageRepository;

    @Override
    public String createChatMessage(MessageDto messageDto) {
        ChatMessage chatMessage = chatMessageRepository.save(messageDto.toEntity());
        return chatMessage.getId();
    }
}
