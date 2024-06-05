package com.example.waggle.domain.chat.application;

import com.example.waggle.domain.chat.persistence.entity.ChatMessage;
import com.example.waggle.domain.member.persistence.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatMessageQueryService {

    Page<ChatMessage> getPagedChatMessages(Member member, Long chatRoomId, Pageable pageable);

}
