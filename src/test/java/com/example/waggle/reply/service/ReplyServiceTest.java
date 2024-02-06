package com.example.waggle.reply.service;

import com.example.waggle.domain.board.service.BoardType;
import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.service.StoryCommandService;
import com.example.waggle.domain.board.story.service.StoryQueryService;
import com.example.waggle.domain.conversation.entity.Comment;
import com.example.waggle.domain.conversation.entity.Reply;
import com.example.waggle.domain.conversation.service.comment.CommentCommandService;
import com.example.waggle.domain.conversation.service.comment.CommentQueryService;
import com.example.waggle.domain.conversation.service.reply.ReplyCommandService;
import com.example.waggle.domain.conversation.service.reply.ReplyQueryService;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.mention.entity.Mention;
import com.example.waggle.domain.mention.repository.MentionRepository;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.comment.CommentRequest;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.reply.ReplyRequest;
import com.example.waggle.web.dto.story.StoryRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@WithMockCustomUser
@Slf4j
class ReplyServiceTest {

    @Autowired
    private StoryCommandService storyService;
    @Autowired
    private StoryQueryService storyQueryService;
    @Autowired
    private MemberCommandService memberService;
    @Autowired
    private CommentCommandService commentService;
    @Autowired
    private CommentQueryService commentQueryService;
    @Autowired
    private ReplyCommandService replyService;
    @Autowired
    private ReplyQueryService replyQueryService;
    @Autowired
    private MentionRepository mentionRepository;
    @Autowired
    DatabaseCleanUp databaseCleanUp;


    MemberRequest.RegisterDto signUpDto1;
    MemberRequest.RegisterDto signUpDto2;
    MemberRequest.RegisterDto signUpDto3;
    MemberRequest.RegisterDto signUpDto4;
    MemberRequest.RegisterDto signUpDto5;


    StoryRequest.Post storyWriteDto1;
    StoryRequest.Post storyWriteDto2;

    CommentRequest.Post commentWriteDto1;
    ReplyRequest.Post replyWriteDto1;
    ReplyRequest.Post replyWriteDto2;

    List<String> mentions1 = new ArrayList<>();
    List<String> mentions2 = new ArrayList<>();


    @BeforeEach
    void setDto() {

        mentions1.add("member1");
        mentions1.add("user2");
        mentions2.add("user3");
        mentions2.add("user4");

        signUpDto1 = MemberRequest.RegisterDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .email("hi")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        signUpDto2 = MemberRequest.RegisterDto.builder()
                .username("user2")
                .password("12345678")
                .nickname("닉네임2")
                .email("hoe")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        signUpDto3 = MemberRequest.RegisterDto.builder()
                .username("user3")
                .password("12345678")
                .nickname("닉네임3")
                .email("zz")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();

        signUpDto4 = MemberRequest.RegisterDto.builder()
                .username("user4")
                .password("12345678")
                .nickname("닉네임4")
                .email("haha")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        signUpDto5 = MemberRequest.RegisterDto.builder()
                .username("user5")
                .password("12345678")
                .nickname("닉네임5")
                .email("ez")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();


        storyWriteDto1 = StoryRequest.Post.builder()
                .content("i love my choco")
                .build();

        storyWriteDto2 = StoryRequest.Post.builder()
                .content("how can i do make he is happy?")
                .build();

        commentWriteDto1 = CommentRequest.Post.builder()
                .content("comment1")
                .build();

        replyWriteDto1 = ReplyRequest.Post.builder()
                .content("reply1")
                .mentionedMembers(mentions1)
                .build();

        replyWriteDto2 = ReplyRequest.Post.builder()
                .content("reply2")
                .mentionedMembers(mentions2)
                .build();


    }

    @AfterEach
    void clean() {
        databaseCleanUp.truncateAllEntity();
    }

    private void setAll() throws IOException {
        //member set
        memberService.signUp(signUpDto1);
        memberService.signUp(signUpDto2);
        memberService.signUp(signUpDto3);
        memberService.signUp(signUpDto4);
        memberService.signUp(signUpDto5);

        //story set
        storyService.createStory(storyWriteDto1, null);
        storyService.createStory(storyWriteDto2, null);

        Story story = storyQueryService.getStories().get(0);

        //comment set
        commentService.createComment(story.getId(), commentWriteDto1, BoardType.STORY);
        //reply set
        List<Comment> comments = commentQueryService.getComments(story.getId());
        replyService.createReply(comments.get(0).getId(), replyWriteDto1);
        replyService.createReply(comments.get(0).getId(), replyWriteDto2);

    }


    @Test
    @Transactional
    void findReplies() throws IOException {
        //given
        setAll();
        Story story = storyQueryService.getStories().get(0);
        List<Comment> comments = commentQueryService.getComments(story.getId());
        //when
        List<Reply> replies = replyQueryService.getReplies(comments.get(0).getId());
        //then
        assertThat(replies.size()).isEqualTo(2);
        assertThat(replies.get(0).getMentions().size()).isEqualTo(2);
    }


    @Test
    void changeReply() throws IOException {
        //given
        setAll();
        Story story = storyQueryService.getStories().get(0);
        List<Comment> comments = commentQueryService.getComments(story.getId());
        List<Reply> replies = replyQueryService.getReplies(comments.get(0).getId());
        //when
        replyService.updateReply(replies.get(0).getId(), replyWriteDto2);
        List<Reply> replyList = replyQueryService.getReplies(comments.get(0).getId());
        //then
        assertThat(replyList.get(0).getContent()).isEqualTo("reply2");
//        assertThat(replies.get(0).getMentionMembers().get(0)).isEqualTo("user3");

    }

    @Test
    @WithMockCustomUser
    @Transactional
    void deleteReply() throws IOException {
        //given
        setAll();
        Story story = storyQueryService.getStories().get(0);
        List<Comment> comments = commentQueryService.getComments(story.getId());
        List<Reply> replies = replyQueryService.getReplies(comments.get(0).getId());

        //when
        replyService.deleteReply(replies.get(0).getId());
        List<Reply> replyList = replyQueryService.getReplies(comments.get(0).getId());
        List<Mention> all = mentionRepository.findAll();
        //then
        assertThat(replyList.size()).isEqualTo(1);
        assertThat(all.size()).isEqualTo(2);
    }

    @Test
    @WithMockCustomUser
    void deleteComments() throws IOException {
        //given
        setAll();
        Story story = storyQueryService.getStories().get(0);
        List<Comment> comments = commentQueryService.getComments(story.getId());
        Long id = comments.get(0).getId();

        //when
        commentService.deleteComment(id);
        List<Reply> replies = replyQueryService.getReplies(id);

        //then
        assertThat(replies.size()).isEqualTo(0);

    }

    @Test
    @WithMockCustomUser
    void deleteBoards() throws IOException {
        //given
        setAll();
        Story story = storyQueryService.getStories().get(0);
        List<Comment> comments = commentQueryService.getComments(story.getId());
        Long id = comments.get(0).getId();

        //when
        storyService.deleteStory(story.getId());
        List<Reply> replies = replyQueryService.getReplies(id);

        //then
        assertThat(replies.size()).isEqualTo(0);


    }
}