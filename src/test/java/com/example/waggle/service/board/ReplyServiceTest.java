package com.example.waggle.service.board;

import com.example.waggle.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.component.DatabaseCleanUp;
import com.example.waggle.dto.board.comment.CommentViewDto;
import com.example.waggle.dto.board.comment.CommentWriteDto;
import com.example.waggle.dto.board.reply.ReplyViewDto;
import com.example.waggle.dto.board.reply.ReplyWriteDto;
import com.example.waggle.dto.board.story.StorySimpleViewDto;
import com.example.waggle.dto.board.story.StoryWriteDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.service.board.util.BoardType;
import com.example.waggle.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ReplyServiceTest {

    @Autowired
    private StoryService storyService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ReplyService replyService;
    @Autowired
    DatabaseCleanUp databaseCleanUp;


    SignUpDto signUpDto1;
    SignUpDto signUpDto2;
    SignUpDto signUpDto3;
    SignUpDto signUpDto4;
    SignUpDto signUpDto5;


    StoryWriteDto storyWriteDto1;
    StoryWriteDto storyWriteDto2;

    CommentWriteDto commentWriteDto1;
    ReplyWriteDto replyWriteDto1;
    ReplyWriteDto replyWriteDto2;

    List<String> mentions1 = new ArrayList<>();
    List<String> mentions2 = new ArrayList<>();



    @BeforeEach
    void setDto() {

        mentions1.add("user1");
        mentions1.add("user2");
        mentions2.add("user3");
        mentions2.add("user4");

        signUpDto1 = SignUpDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        signUpDto2 = SignUpDto.builder()
                .username("user1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        signUpDto3 = SignUpDto.builder()
                .username("user2")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        signUpDto4 = SignUpDto.builder()
                .username("user3")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        signUpDto5 = SignUpDto.builder()
                .username("user4")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();



        storyWriteDto1 = StoryWriteDto.builder()
                .content("i love my choco")
                .thumbnail("www.waggle")
                .build();

        storyWriteDto2 = StoryWriteDto.builder()
                .content("how can i do make he is happy?")
                .thumbnail("www.waggle")
                .build();

        commentWriteDto1 = CommentWriteDto.builder()
                .content("comment1")
                .build();

        replyWriteDto1 = ReplyWriteDto.builder()
                .content("reply1")
                .mentionMembers(mentions1)
                .build();

        replyWriteDto2 = ReplyWriteDto.builder()
                .content("reply2")
                .mentionMembers(mentions2)
                .build();


    }
    @AfterEach
    void clean() {
        databaseCleanUp.truncateAllEntity();
    }

    private void setAll() {
        //member set
        memberService.signUp(signUpDto1);
        memberService.signUp(signUpDto2);
        memberService.signUp(signUpDto3);
        memberService.signUp(signUpDto4);
        memberService.signUp(signUpDto5);

        //story set
        storyService.saveStory(storyWriteDto1);
        storyService.saveStory(storyWriteDto2);

        StorySimpleViewDto storySimpleViewDto = storyService.findAllStory().get(0);

        //comment set
        commentService.saveComment(storySimpleViewDto.getId(), commentWriteDto1, BoardType.STORY);
        //reply set
        List<CommentViewDto> comments = commentService.findComments(storySimpleViewDto.getId());
        replyService.saveReply(comments.get(0), replyWriteDto1);

    }


    @Test
    @WithMockCustomUser
    void findReplies() {
        //given
        setAll();
        StorySimpleViewDto storySimpleViewDto = storyService.findAllStory().get(0);
        List<CommentViewDto> comments = commentService.findComments(storySimpleViewDto.getId());
        //when
        List<ReplyViewDto> replies = replyService.findReplies(comments.get(0).getId());
        //then
        assertThat(replies.size()).isEqualTo(1);
        assertThat(replies.get(0).getMentionMembers().size()).isEqualTo(2);
    }


    @Test
    @WithMockCustomUser
    void changeReply() {
        //given
        setAll();
        StorySimpleViewDto storySimpleViewDto = storyService.findAllStory().get(0);
        List<CommentViewDto> comments = commentService.findComments(storySimpleViewDto.getId());
        List<ReplyViewDto> replies = replyService.findReplies(comments.get(0).getId());
        //when
        replyService.changeReply(replies.get(0), replyWriteDto2);
        //then
        List<ReplyViewDto> editReplies = replyService.findReplies(comments.get(0).getId());
        assertThat(editReplies.get(0).getContent()).isEqualTo("reply2");
        assertThat(editReplies.get(0).getMentionMembers().get(0)).isEqualTo("user3");

    }

    @Test
    @WithMockCustomUser
    void deleteReply() {
        //given
        setAll();
        StorySimpleViewDto storySimpleViewDto = storyService.findAllStory().get(0);
        List<CommentViewDto> comments = commentService.findComments(storySimpleViewDto.getId());
        List<ReplyViewDto> replies = replyService.findReplies(comments.get(0).getId());

        //when
        replyService.deleteReply(replies.get(0));

        //then
        List<ReplyViewDto> afterDeleteReplies = replyService.findReplies(comments.get(0).getId());
        assertThat(afterDeleteReplies.size()).isEqualTo(0);
    }
}