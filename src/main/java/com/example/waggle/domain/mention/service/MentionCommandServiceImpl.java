package com.example.waggle.domain.mention.service;

import com.example.waggle.domain.conversation.Conversation;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.mention.entity.Mention;
import com.example.waggle.domain.mention.repository.MentionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class MentionCommandServiceImpl implements MentionCommandService {
    private final MentionRepository mentionRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long createMentions(Conversation conversation, List<String> mentions) {
        mentions.stream().forEach(mention -> {
            if (memberRepository.existsByNickname(mention)) {
                Mention build = Mention.builder().conversation(conversation).mentionedNickname(mention).build();
                mentionRepository.save(build);
            }
        });
        return conversation.getId();
    }

    @Override
    public Long updateMentions(Conversation conversation, List<String> mentions) {
        mentionRepository.deleteByConversationId(conversation.getId());
        createMentions(conversation, mentions);
        return conversation.getId();
    }

    @Override
    public void deleteMentions(Conversation conversation) {
        mentionRepository.deleteByConversationId(conversation.getId());
    }
}
