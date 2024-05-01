package com.example.waggle.domain.chat.service;

import com.example.waggle.domain.chat.entity.ChatMessage;
import com.example.waggle.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatMessageQueryService {

    Page<ChatMessage> getPagedChatMessages(Member member, Long chatRoomId, Pageable pageable);

}
