package com.example.waggle.comment.service;

import com.example.waggle.domain.board.service.BoardType;
import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.service.StoryCommandService;
import com.example.waggle.domain.board.story.service.StoryQueryService;
import com.example.waggle.domain.conversation.entity.Comment;
import com.example.waggle.domain.conversation.service.comment.CommentCommandService;
import com.example.waggle.domain.conversation.service.comment.CommentQueryService;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.comment.CommentRequest;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.web.dto.member.MemberRequest;
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
class CommentServiceTest {

    @Autowired
    private StoryQueryService storyService;
    @Autowired
    private StoryCommandService storyCommandService;
    @Autowired
    private MemberCommandService memberService;
    @Autowired
    private CommentQueryService commentService;
    @Autowired
    private CommentCommandService commentCommandService;
    @Autowired
    DatabaseCleanUp databaseCleanUp;


    MemberRequest.RegisterDto signUpDto1;
    MemberRequest.RegisterDto signUpDto2;


    StoryRequest.Post storyWriteDto1;
    StoryRequest.Post storyWriteDto2;

    CommentRequest.Post commentWriteDto1;
    CommentRequest.Post commentWriteDto2;

    List<String> tags1 = new ArrayList<>();
    List<String> tags2 = new ArrayList<>();
    List<String> medias1 = new ArrayList<>();
    List<String> medias2 = new ArrayList<>();


    @BeforeEach
    void setDto() {
        tags1.add("choco");
        tags1.add("poodle");
        tags2.add("poodle");

        medias1.add("media1");
        medias1.add("mediamedia1");
        medias2.add("media2");
        medias2.add("mediamedia2");

        signUpDto1 = MemberRequest.RegisterDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .email("ertyuio")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();

        signUpDto2 = MemberRequest.RegisterDto.builder()
                .username("member2")
                .password("12345678")
                .nickname("닉네임2")
                .email("78347dj")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();

        storyWriteDto1 = StoryRequest.Post.builder()
                .content("i love my choco")
                .hashtags(tags1)
                .medias(medias1)
                .build();

        storyWriteDto2 = StoryRequest.Post.builder()
                .content("how can i do make he is happy?")
                .hashtags(tags2)
                .medias(medias2)
                .build();

        commentWriteDto1 = CommentRequest.Post.builder()
                .content("comment1")
                .build();

        commentWriteDto2 = CommentRequest.Post.builder()
                .content("comment2")
                .build();


    }

    @AfterEach
    void clean() {
        databaseCleanUp.truncateAllEntity();
    }

    private void setBoardAndMember() throws IOException {
        //member set
        memberService.signUp(signUpDto1);
        memberService.signUp(signUpDto2);

        //story set
        storyCommandService.createStory(storyWriteDto1, null);
        storyCommandService.createStory(storyWriteDto2, null);
    }

    @Test
    @WithMockCustomUser
    void saveComment() throws IOException {
        //given
        setBoardAndMember();
        Story story = storyService.getStories().get(0);

        //when
        commentCommandService.createComment(story.getId(), commentWriteDto1, BoardType.STORY);
        List<Comment> comments = commentService.getComments(story.getId());
        //then
        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0).getContent()).isEqualTo("comment1");
    }

    @Test
    @WithMockCustomUser
    void editCommentV1() throws IOException {
        //given
        setBoardAndMember();
        Story story = storyService.getStories().get(0);
        commentCommandService.createComment(story.getId(), commentWriteDto1, BoardType.STORY);
        List<Comment> comments = commentService.getComments(story.getId());

        //when
        commentCommandService.updateComment(comments.get(0).getId(), commentWriteDto2);
        List<Comment> commentList = commentService.getComments(story.getId());

        //then
        assertThat(commentList.get(0).getContent()).isEqualTo("comment2");
    }


    @Test
    @WithMockCustomUser
    void deleteComment() throws IOException {
        //given
        setBoardAndMember();
        Story story = storyService.getStories().get(0);

        //when
        commentCommandService.createComment(story.getId(), commentWriteDto1, BoardType.STORY);
        List<Comment> comments = commentService.getComments(story.getId());
        commentCommandService.deleteComment(comments.get(0).getId());
        List<Comment> commentList = commentService.getComments(story.getId());

        //then
        assertThat(commentList.size()).isEqualTo(0);

    }

    @Test
    @WithMockCustomUser
    void deleteStory() throws IOException {
        //given
        setBoardAndMember();
        Story story = storyService.getStories().get(0);

        //when
        commentCommandService.createComment(story.getId(), commentWriteDto1, BoardType.STORY);
        storyCommandService.deleteStory(story.getId());

        List<Comment> comments = commentService.getComments(story.getId());
        //then
        assertThat(comments.size()).isEqualTo(0);

    }
}