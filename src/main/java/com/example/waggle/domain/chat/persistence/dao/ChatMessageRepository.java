package com.example.waggle.domain.chat.persistence.dao;

import com.example.waggle.domain.chat.persistence.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    @Query(value = "{'chatRoomId': ?0, 'sendTime': {$gt: ?1}}", count = true)
    long countByChatRoomIdAndSendTimeAfter(Long chatRoomId, long lastAccessTime);

    @Query(value = "{'chatRoomId': ?0}", sort = "{'sendTime': -1}")
    Page<ChatMessage> findRecentMessagesByChatRoomId(Long chatRoomId, Pageable pageable);

    @Query(value = "{'chatRoomId': ?0, 'sendTime': {$gt: ?1}}", sort = "{'sendTime': -1}")
    Page<ChatMessage> findMessagesByChatRoomIdAfterMemberEnterTime(Long chatRoomId, Long memberEnterTime,
                                                                   Pageable pageable);

}
