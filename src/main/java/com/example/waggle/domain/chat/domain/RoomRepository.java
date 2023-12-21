package com.example.waggle.domain.chat.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, String> {
}
