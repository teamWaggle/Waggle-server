package com.example.waggle.domain.mention.service;

import com.example.waggle.domain.conversation.Conversation;

import java.util.List;

public interface MentionCommandService {
    Long createMentions(Conversation conversation, List<String> mentions);

    Long updateMentions(Conversation conversation, List<String> mentions);

    void deleteMentions(Conversation conversation);
}
