package com.example.waggle.service.board;

import com.example.waggle.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.component.DatabaseCleanUp;
import com.example.waggle.dto.board.comment.CommentViewDto;
import com.example.waggle.dto.board.comment.CommentWriteDto;
import com.example.waggle.dto.board.reply.ReplyViewDto;
import com.example.waggle.dto.board.reply.ReplyWriteDto;
import com.example.waggle.dto.board.story.StoryWriteDto;
import com.example.waggle.dto.member.SignUpDto;
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

        //story set
        storyService.saveStory(storyWriteDto1);
        storyService.saveStory(storyWriteDto2);

        //comment set
        commentService.saveComment(6L, commentWriteDto1, "story");
        //reply set
        List<CommentViewDto> comments = commentService.findComments(6L);
        replyService.saveReply(comments.get(0), replyWriteDto1);

    }


    @Test
    @WithMockCustomUser
    void findReplies() {
        //given
        setAll();
        //when
        List<ReplyViewDto> replies = replyService.findReplies(1L);
        //then
        assertThat(replies.size()).isEqualTo(1);
        assertThat(replies.get(0).getMentionMembers().size()).isEqualTo(2);
    }


    @Test
    @WithMockCustomUser
    void changeReply() {
        //given
        setAll();
        List<ReplyViewDto> replies = replyService.findReplies(1L);
        //when
        replyService.changeReply(replies.get(0), replyWriteDto2);
        //then
        List<ReplyViewDto> editReplies = replyService.findReplies(1L);
        assertThat(editReplies.get(0).getContent()).isEqualTo("reply2");
        assertThat(editReplies.get(0).getMentionMembers().get(0)).isEqualTo("user3");

    }

    @Test
    @WithMockCustomUser
    void deleteReply() {
        //given
        setAll();
        List<ReplyViewDto> replies = replyService.findReplies(1L);

        //when
        replyService.deleteReply(replies.get(0));

        //then
        List<ReplyViewDto> afterDeleteReplies = replyService.findReplies(1L);
        assertThat(afterDeleteReplies.size()).isEqualTo(0);
    }
}