package com.example.waggle.domain.chat.repository;

import com.example.waggle.domain.chat.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, String> {
}
