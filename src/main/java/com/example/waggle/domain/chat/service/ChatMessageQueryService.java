package com.example.waggle.domain.chat.service;

import com.example.waggle.domain.chat.entity.ChatMessage;
import java.awt.print.Pageable;
import java.util.List;

public interface ChatMessageQueryService {

    List<ChatMessage> getPagedChatMessages(Pageable pageable);

}
