package com.example.waggle.repository.board.comment;

import com.example.waggle.board.story.domain.Story;
import com.example.waggle.comment.domain.Comment;
import com.example.waggle.comment.repository.CommentRepository;
import com.example.waggle.memberMention.domain.MemberMention;
import com.example.waggle.memberMention.repository.MemberMentionRepository;
import com.example.waggle.comment.domain.Reply;
import com.example.waggle.board.story.repository.StoryRepository;
import com.example.waggle.comment.repository.ReplyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentRepositoryTest {

    @Autowired
    StoryRepository storyRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    MemberMentionRepository memberMentionRepository;

    //board-comment-reply check
    @Test
    @Transactional
    @Rollback
    void test() {

        Story testStory = Story.builder().thumbnail("@hann").content("Board test Repository").build();
        storyRepository.save(testStory);

        Reply reply1 = Reply.builder().content("reply!").build();
        Reply reply2 = Reply.builder().content("reply!!").build();
        List<Reply> replies = new ArrayList<>();

        replies.add(reply1);
        replies.add(reply2);

        for (Reply reply : replies) {
            replyRepository.save(reply);
        }

        Comment hello = Comment.builder().board(testStory).content("hello").build();
        Comment halo = Comment.builder().board(testStory).content("halo").replies(replies).build();
        List<Comment> comments = new ArrayList<>();
        comments.add(hello);
        comments.add(halo);

        for (Comment comment : comments) {
            commentRepository.save(comment);
        }
        List<Comment> findAllComment = commentRepository.findAll();
        assertThat(findAllComment.size()).isEqualTo(2);
        List<Comment> collect = findAllComment.stream().filter(c -> c.getReplies().size() != 0).collect(Collectors.toList());
        assertThat(collect.size()).isEqualTo(1);
    }

    //board-comment-reply-memberMention check
    @Test
    @Transactional
    @Rollback
    void testMention() {

        Story testStory = Story.builder().thumbnail("@hann").content("Board test Repository").build();
        Story save = storyRepository.save(testStory);


        //comment
        Comment hello = Comment.builder().board(testStory).content("hello").build();
        Comment halo = Comment.builder().board(testStory).content("halo").build();
        //save comment
        List<Comment> comments = new ArrayList<>();
        comments.add(hello);
        comments.add(halo);
        for (Comment comment : comments) {
            commentRepository.save(comment);
        }

        //reply
        Reply reply1 = Reply.builder().content("reply!").build();
        Reply reply2 = Reply.builder().content("reply!!").build();
        //save reply
        List<Reply> replies = new ArrayList<>();
        replies.add(reply1);
        replies.add(reply2);

        for (Reply reply : replies) {
            replyRepository.save(reply);
        }

        //comment-reply link
        hello.addReply(reply1);
        halo.addReply(reply2);

        //memberMention
        MemberMention hann = MemberMention.builder().username("hann").build();
        memberMentionRepository.save(hann);

        //reply-memberMention link
        reply1.addMemberMention(hann);

        List<Reply> all = replyRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
        assertThat(all.get(0).getComment().getContent()).isEqualTo("hello");


    }

}