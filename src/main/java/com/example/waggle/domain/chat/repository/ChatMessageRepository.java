package com.example.waggle.domain.chat.repository;

import com.example.waggle.domain.chat.entity.ChatMessage;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    @Query("{'chatRoomId': ?0, 'sendTime': {$gt: ?1}}")
    long countByChatRoomIdAndSendTimeAfter(Long chatRoomId, LocalDateTime lastAccessTime);

    @Query(value = "{'chatRoomId': ?0}", sort = "{'sendTime': -1}")
    Optional<ChatMessage> findTopByChatRoomIdOrderBySendTimeDesc(Long chatRoomId);

    @Query(value = "{'chatRoomId': ?0}")
    Page<ChatMessage> findByChatRoomIdSortedBySendTimeDesc(Long chatRoomId, Pageable pageable);

}
