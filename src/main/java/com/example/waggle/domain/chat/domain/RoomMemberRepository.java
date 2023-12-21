package com.example.waggle.domain.chat.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
}
