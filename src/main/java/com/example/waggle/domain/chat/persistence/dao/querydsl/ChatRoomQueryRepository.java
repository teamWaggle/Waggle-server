package com.example.waggle.domain.chat.persistence.dao.querydsl;

import com.example.waggle.domain.chat.persistence.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomQueryRepository {

    Page<ChatRoom> findByMemberId(Long memberId, Pageable pageable);

    Long countChatRoomsByMemberId(Long memberId);

    Page<ChatRoom> searchByKeyword(String keyword, Pageable pageable);

}