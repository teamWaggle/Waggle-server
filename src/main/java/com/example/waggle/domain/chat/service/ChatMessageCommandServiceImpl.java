package com.example.waggle.domain.chat.service;

import com.example.waggle.domain.chat.dto.MessageDto;
import com.example.waggle.domain.chat.entity.ChatMessage;
import com.example.waggle.domain.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageCommandServiceImpl implements ChatMessageCommandService {

    private final ChatMessageRepository chatMessageRepository;

    @Override
    public String createChatMessage(MessageDto messageDto) {
        ChatMessage chatMessageDto = messageDto.toEntity();
        ChatMessage chatMessage = chatMessageRepository.save(chatMessageDto);
        log.info("chatMessage = {}", chatMessage);
        return chatMessage.getId();
    }
}
