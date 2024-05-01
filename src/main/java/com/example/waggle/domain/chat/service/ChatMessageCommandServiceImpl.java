package com.example.waggle.domain.chat.service;

import com.example.waggle.web.dto.chat.ChatMessageDto;
import com.example.waggle.domain.chat.entity.ChatMessage;
import com.example.waggle.domain.chat.repository.ChatMessageRepository;
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
