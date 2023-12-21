package com.example.waggle.domain.chat.repository;

import com.example.waggle.domain.chat.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoChatRepository extends MongoRepository<Chat, Long> {
}
