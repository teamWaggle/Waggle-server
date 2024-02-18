package com.example.waggle.domain.mention.repository;

import com.example.waggle.domain.conversation.Conversation;
import com.example.waggle.domain.mention.entity.Mention;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentionRepository extends JpaRepository<Mention, Long> {
    List<Mention> findByConversationId(Long conversationId);

    void deleteByConversationId(Long conversationId);

    void deleteAllByMentionedNickname(String mentionedNickname);

    void deleteAllByConversation(Conversation conversation);

}