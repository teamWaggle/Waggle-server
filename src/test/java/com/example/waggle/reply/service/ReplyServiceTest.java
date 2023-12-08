package com.example.waggle.reply.service;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.service.StoryCommandService;
import com.example.waggle.domain.board.story.service.StoryQueryService;
import com.example.waggle.domain.comment.entity.Comment;
import com.example.waggle.domain.comment.entity.Reply;
import com.example.waggle.domain.comment.service.comment.CommentCommandService;
import com.example.waggle.domain.comment.service.comment.CommentQueryService;
import com.example.waggle.domain.comment.service.reply.ReplyCommandService;
import com.example.waggle.domain.comment.service.reply.ReplyQueryService;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.global.util.service.BoardType;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
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
    DatabaseCleanUp databaseCleanUp;


    MemberRequest.RegisterRequestDto signUpDto1;
    MemberRequest.RegisterRequestDto signUpDto2;
    MemberRequest.RegisterRequestDto signUpDto3;
    MemberRequest.RegisterRequestDto signUpDto4;
    MemberRequest.RegisterRequestDto signUpDto5;


    StoryRequest.Post storyWriteDto1;
    StoryRequest.Post storyWriteDto2;

    CommentRequest.Post commentWriteDto1;
    ReplyRequest.Post replyWriteDto1;
    ReplyRequest.Post replyWriteDto2;

    List<String> mentions1 = new ArrayList<>();
    List<String> mentions2 = new ArrayList<>();



    @BeforeEach
    void setDto() {

        mentions1.add("user1");
        mentions1.add("user2");
        mentions2.add("user3");
        mentions2.add("user4");

        signUpDto1 = MemberRequest.RegisterRequestDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        signUpDto2 = MemberRequest.RegisterRequestDto.builder()
                .username("user1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        signUpDto3 = MemberRequest.RegisterRequestDto.builder()
                .username("user2")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        signUpDto4 = MemberRequest.RegisterRequestDto.builder()
                .username("user3")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        signUpDto5 = MemberRequest.RegisterRequestDto.builder()
                .username("user4")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();



        storyWriteDto1 = StoryRequest.Post.builder()
                .content("i love my choco")
                .thumbnail("www.waggle")
                .build();

        storyWriteDto2 = StoryRequest.Post.builder()
                .content("how can i do make he is happy?")
                .thumbnail("www.waggle")
                .build();

        commentWriteDto1 = CommentRequest.Post.builder()
                .content("comment1")
                .build();

        replyWriteDto1 = ReplyRequest.Post.builder()
                .content("reply1")
                .mentions(mentions1)
                .build();

        replyWriteDto2 = ReplyRequest.Post.builder()
                .content("reply2")
                .mentions(mentions2)
                .build();


    }
    @AfterEach
    void clean() {
        databaseCleanUp.truncateAllEntity();
    }

    private void setAll() throws IOException {
        //member set
        memberService.signUp(signUpDto1, null);
        memberService.signUp(signUpDto2, null);
        memberService.signUp(signUpDto3, null);
        memberService.signUp(signUpDto4, null);
        memberService.signUp(signUpDto5, null);

        //story set
        storyService.createStory(storyWriteDto1);
        storyService.createStory(storyWriteDto2);

        Story story = storyQueryService.getStories().get(0);

        //comment set
        commentService.createComment(story.getId(), commentWriteDto1, BoardType.STORY);
        //reply set
        List<Comment> comments = commentQueryService.getComments(story.getId());
        replyService.createReply(comments.get(0).getId(), replyWriteDto1);

    }


    @Test
    @WithMockCustomUser
    void findReplies() throws IOException {
        //given
        setAll();
        Story story = storyQueryService.getStories().get(0);
        List<Comment> comments = commentQueryService.getComments(story.getId());
        //when
        List<Reply> replies = replyQueryService.getReplies(comments.get(0).getId());
        //then
        assertThat(replies.size()).isEqualTo(1);
        assertThat(replies.get(0).getMentions().size()).isEqualTo(2);
    }


    @Test
    @WithMockCustomUser
    void changeReply() throws IOException {
        //given
        setAll();
        Story story = storyQueryService.getStories().get(0);
        List<Comment> comments = commentQueryService.getComments(story.getId());
        List<Reply> replies = replyQueryService.getReplies(comments.get(0).getId());
        //when
        replyService.updateReply(replies.get(0).getId(), replyWriteDto2);
        //then
        assertThat(replies.get(0).getContent()).isEqualTo("reply2");
//        assertThat(replies.get(0).getMentionMembers().get(0)).isEqualTo("user3");

    }

    @Test
    @WithMockCustomUser
    void deleteReply() throws IOException {
        //given
        setAll();
        Story story = storyQueryService.getStories().get(0);
        List<Comment> comments = commentQueryService.getComments(story.getId());
        List<Reply> replies = replyQueryService.getReplies(comments.get(0).getId());

        //when
        replyService.deleteReply(replies.get(0).getId());

        //then

        assertThat(replies.size()).isEqualTo(0);
    }
}