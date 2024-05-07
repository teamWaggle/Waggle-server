package com.example.waggle.domain.chat.persistence.dao;

import com.example.waggle.domain.chat.persistence.entity.ChatRoomMember;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    Optional<ChatRoomMember> findByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);

}