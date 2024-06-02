package com.example.waggle.domain.chat.persistence.dao;

import com.example.waggle.domain.chat.persistence.dao.querydsl.ChatRoomQueryRepository;
import com.example.waggle.domain.chat.persistence.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomQueryRepository {
}
