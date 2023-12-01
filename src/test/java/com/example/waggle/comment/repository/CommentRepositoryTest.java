package com.example.waggle.comment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.repository.StoryRepository;
import com.example.waggle.domain.comment.entity.Comment;
import com.example.waggle.domain.comment.entity.Reply;
import com.example.waggle.domain.comment.repository.CommentRepository;
import com.example.waggle.domain.comment.repository.ReplyRepository;
import com.example.waggle.domain.mention.entity.Mention;
import com.example.waggle.domain.mention.repository.MentionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

    //board-comment-reply check
    @Test
    @Transactional
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
        Mention hann = Mention.builder().username("hann").build();
        mentionRepository.save(hann);

        //reply-memberMention link
        reply1.addMention(hann);

        List<Reply> all = replyRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
        assertThat(all.get(0).getComment().getContent()).isEqualTo("hello");


    }

}