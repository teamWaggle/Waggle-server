package com.example.waggle.comment.service;

import com.example.waggle.domain.board.story.service.StoryCommandService;
import com.example.waggle.domain.board.story.service.StoryQueryService;
import com.example.waggle.domain.comment.service.comment.CommentCommandService;
import com.example.waggle.domain.comment.service.comment.CommentQueryService;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.global.util.service.BoardType;
import com.example.waggle.web.dto.comment.CommentViewDto;
import com.example.waggle.web.dto.comment.CommentWriteDto;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.story.StorySummaryDto;
import com.example.waggle.web.dto.story.StoryWriteDto;
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


    MemberRequest.RegisterRequestDto signUpDto1;
    MemberRequest.RegisterRequestDto signUpDto2;


    StoryWriteDto storyWriteDto1;
    StoryWriteDto storyWriteDto2;

    CommentWriteDto commentWriteDto1;
    CommentWriteDto commentWriteDto2;

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

        signUpDto1 = MemberRequest.RegisterRequestDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();

        signUpDto2 = MemberRequest.RegisterRequestDto.builder()
                .username("member2")
                .password("12345678")
                .nickname("닉네임2")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();

        storyWriteDto1 = StoryWriteDto.builder()
                .content("i love my choco")
                .hashtags(tags1)
                .medias(medias1)
                .thumbnail("www.waggle")
                .build();

        storyWriteDto2 = StoryWriteDto.builder()
                .content("how can i do make he is happy?")
                .hashtags(tags2)
                .medias(medias2)
                .thumbnail("www.waggle")
                .build();

        commentWriteDto1 = CommentWriteDto.builder()
                .content("comment1")
                .build();

        commentWriteDto2 = CommentWriteDto.builder()
                .content("comment2")
                .build();


    }
    @AfterEach
    void clean() {
        databaseCleanUp.truncateAllEntity();
    }

    private void setBoardAndMember() throws IOException {
        //member set
        memberService.signUp(signUpDto1, null);
        memberService.signUp(signUpDto2, null);

        //story set
        storyCommandService.createStory(storyWriteDto1, new ArrayList<>(), null);
        storyCommandService.createStory(storyWriteDto2, new ArrayList<>(), null);
    }

    @Test
    @WithMockCustomUser
    void saveComment() throws IOException {
        //given
        setBoardAndMember();
        StorySummaryDto storySummaryDto = storyService.getStories().get(0);

        //when
        commentCommandService.createComment(storySummaryDto.getId(), commentWriteDto1, BoardType.STORY);
        List<CommentViewDto> comments = commentService.getComments(storySummaryDto.getId());
        //then
        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0).getContent()).isEqualTo("comment1");
        assertThat(comments.get(0).getUsername()).isEqualTo("member1");
    }

    @Test
    @WithMockCustomUser
    void editCommentV1() throws IOException {
        //given
        setBoardAndMember();
        StorySummaryDto storySummaryDto = storyService.getStories().get(0);
        commentCommandService.createComment(storySummaryDto.getId(), commentWriteDto1, BoardType.STORY);
        List<CommentViewDto> comments = commentService.getComments(storySummaryDto.getId());
        List<CommentViewDto> editComments = new ArrayList<>();

        //when
        if (commentService.validateMember(comments.get(0).getId())) {
            commentCommandService.updateComment(comments.get(0).getId(), commentWriteDto2);
             editComments = commentService.getComments(storySummaryDto.getId());
        }

        //then
        log.info("comment = {}", editComments.get(0).getContent());
        assertThat(editComments.get(0).getContent()).isEqualTo("comment2");
    }


    @Test
    @WithMockCustomUser
    void deleteComment() throws IOException {
        //given
        setBoardAndMember();
        StorySummaryDto storySummaryDto = storyService.getStories().get(0);

        commentCommandService.createComment(storySummaryDto.getId(), commentWriteDto1, BoardType.STORY);
        List<CommentViewDto> comments = commentService.getComments(storySummaryDto.getId());

        //when
        log.info("boardId is {}", comments.get(0).getId());
        commentCommandService.deleteComment(comments.get(0).getId());
        List<CommentViewDto> deleteComments = commentService.getComments(storySummaryDto.getId());
        //then
        assertThat(deleteComments.size()).isEqualTo(0);

    }
}