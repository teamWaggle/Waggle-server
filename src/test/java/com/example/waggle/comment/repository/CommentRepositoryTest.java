package com.example.waggle.comment.repository;

import com.example.waggle.domain.board.story.repository.StoryRepository;
import com.example.waggle.domain.conversation.repository.CommentRepository;
import com.example.waggle.domain.conversation.repository.ReplyRepository;
import com.example.waggle.domain.mention.repository.MentionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommentRepositoryTest {

    @Autowired
    StoryRepository storyRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    MentionRepository mentionRepository;


}