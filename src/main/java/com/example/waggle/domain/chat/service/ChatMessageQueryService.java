package com.example.waggle.domain.chat.service;

import com.example.waggle.domain.chat.entity.ChatMessage;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.annotation.auth.AuthUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatMessageQueryService {

    Page<ChatMessage> getPagedChatMessages(@AuthUser Member member, Long chatRoomId, Pageable pageable);

}
