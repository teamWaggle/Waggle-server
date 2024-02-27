package com.example.waggle.reply.service;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.service.StoryCommandService;
import com.example.waggle.domain.board.story.service.StoryQueryService;
import com.example.waggle.domain.conversation.entity.Comment;
import com.example.waggle.domain.conversation.entity.Reply;
import com.example.waggle.domain.conversation.service.comment.CommentCommandService;
import com.example.waggle.domain.conversation.service.comment.CommentQueryService;
import com.example.waggle.domain.conversation.service.reply.ReplyCommandService;
import com.example.waggle.domain.conversation.service.reply.ReplyQueryService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.mention.repository.MentionRepository;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.comment.CommentRequest.CommentCreateDto;
import com.example.waggle.web.dto.member.MemberRequest.MemberCredentialsDto;
import com.example.waggle.web.dto.reply.ReplyRequest.ReplyCreateDto;
import com.example.waggle.web.dto.story.StoryRequest.StoryCreateDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    private MentionRepository mentionRepository;
    @Autowired
    private MemberQueryService memberQueryService;
    @Autowired
    DatabaseCleanUp databaseCleanUp;


    MemberCredentialsDto signUpDto1;
    MemberCredentialsDto signUpDto2;
    MemberCredentialsDto signUpDto3;
    MemberCredentialsDto signUpDto4;
    MemberCredentialsDto signUpDto5;


    StoryCreateDto storyWriteDto1;
    StoryCreateDto storyWriteDto2;

    CommentCreateDto commentWriteDto1;
    ReplyCreateDto replyWriteDto1;
    ReplyCreateDto replyWriteDto2;

    List<String> mentions1 = new ArrayList<>();
    List<String> mentions2 = new ArrayList<>();
    Member member;


    @BeforeEach
    void setDto() {

        mentions1.add("member1");
        mentions1.add("user2");
        mentions2.add("user3");
        mentions2.add("user4");

        signUpDto1 = MemberCredentialsDto.builder()
                .password("12345678")
                .email("hi")
                .build();
        signUpDto2 = MemberCredentialsDto.builder()
                .password("12345678")
                .email("hoe")
                .build();
        signUpDto3 = MemberCredentialsDto.builder()
                .password("12345678")
                .email("zz")
                .build();

        signUpDto4 = MemberCredentialsDto.builder()
                .password("12345678")
                .email("haha")
                .build();
        signUpDto5 = MemberCredentialsDto.builder()
                .password("12345678")
                .email("ez")
                .build();

        storyWriteDto1 = StoryCreateDto.builder()
                .content("i love my choco")
                .build();

        storyWriteDto2 = StoryCreateDto.builder()
                .content("how can i do make he is happy?")
                .build();

        commentWriteDto1 = CommentCreateDto.builder()
                .content("comment1")
                .build();

        replyWriteDto1 = ReplyCreateDto.builder()
                .content("reply1")
                .build();

        replyWriteDto2 = ReplyCreateDto.builder()
                .content("reply2")
                .build();


    }

    @AfterEach
    void clean() {
        databaseCleanUp.truncateAllEntity();
    }

    private void setAll() {
        //member set
        Long memberId = memberService.signUp(signUpDto1);
        memberService.signUp(signUpDto2);
        memberService.signUp(signUpDto3);
        memberService.signUp(signUpDto4);
        memberService.signUp(signUpDto5);
        member = memberQueryService.getMemberById(memberId);

        //story set
        storyService.createStoryByUsername(storyWriteDto1, null, member.getUsername());
        storyService.createStoryByUsername(storyWriteDto2, null, member.getUsername());

        Story story = storyQueryService.getStories().get(0);

        //comment set
        commentService.createCommentByUsername(story.getId(), commentWriteDto1, member.getUsername());
        //reply set
        List<Comment> comments = commentQueryService.getComments(story.getId());
        replyService.createReplyByUsername(comments.get(0).getId(), replyWriteDto1, member.getUsername());
        replyService.createReplyByUsername(comments.get(0).getId(), replyWriteDto2, member.getUsername());

    }


    @Test
    void findReplies() {
        //given
        setAll();
        Story story = storyQueryService.getStories().get(0);
        List<Comment> comments = commentQueryService.getComments(story.getId());
        //when
        List<Reply> replies = replyQueryService.getReplies(comments.get(0).getId());
        //then
        assertThat(replies.size()).isEqualTo(2);
    }


    @Test
    void changeReply() {
        //given
        setAll();
        Story story = storyQueryService.getStories().get(0);
        List<Comment> comments = commentQueryService.getComments(story.getId());
        List<Reply> replies = replyQueryService.getReplies(comments.get(0).getId());
        //when
        replyService.updateReplyByUsername(replies.get(0).getId(), member.getUsername(), replyWriteDto2);
        List<Reply> replyList = replyQueryService.getReplies(comments.get(0).getId());
        //then
        assertThat(replyList.get(0).getContent()).isEqualTo("reply2");
    }

    @Test
    void deleteReply() {
        //given
        setAll();
        Story story = storyQueryService.getStories().get(0);
        List<Comment> comments = commentQueryService.getComments(story.getId());
        List<Reply> replies = replyQueryService.getReplies(comments.get(0).getId());

        //when
        replyService.deleteReply(replies.get(0).getId(), member.getUsername());
        List<Reply> replyList = replyQueryService.getReplies(comments.get(0).getId());
        //then
        assertThat(replyList.size()).isEqualTo(1);
    }

    @Test
    void deleteComments() {
        //given
        setAll();
        Story story = storyQueryService.getStories().get(0);
        List<Comment> comments = commentQueryService.getComments(story.getId());
        Long id = comments.get(0).getId();

        //when
        commentService.deleteCommentByUsername(id, member.getUsername());
        List<Reply> replies = replyQueryService.getReplies(id);

        //then
        assertThat(replies.size()).isEqualTo(0);

    }

    @Test
    void deleteBoards() {
        //given
        setAll();
        Story story = storyQueryService.getStories().get(0);
        List<Comment> comments = commentQueryService.getComments(story.getId());
        Long id = comments.get(0).getId();

        //when
        storyService.deleteStoryByUsername(story.getId(), member.getUsername());
        List<Reply> replies = replyQueryService.getReplies(id);

        //then
        assertThat(replies.size()).isEqualTo(0);

    }
}