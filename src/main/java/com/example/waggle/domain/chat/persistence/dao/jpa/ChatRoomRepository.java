package com.example.waggle.domain.chat.persistence.dao.jpa;

import com.example.waggle.domain.chat.persistence.dao.querydsl.ChatRoomQueryRepository;
import com.example.waggle.domain.chat.persistence.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomQueryRepository {

    @Query("SELECT cr FROM ChatRoom cr JOIN cr.chatRoomMembers crm WHERE crm.member.id = :memberId")
    Page<ChatRoom> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);

}
