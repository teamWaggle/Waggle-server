package com.example.waggle.domain.chat.repository;

import com.example.waggle.domain.chat.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
}
