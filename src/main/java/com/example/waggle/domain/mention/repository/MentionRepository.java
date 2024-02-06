package com.example.waggle.domain.mention.repository;

import com.example.waggle.domain.mention.entity.Mention;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentionRepository extends JpaRepository<Mention, Long> {
    List<Mention> findByConversationId(Long conversationId);

    void deleteByConversationId(Long conversationId);
}