package com.example.waggle.service.board;

import com.example.waggle.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.board.story.service.StoryService;
import com.example.waggle.comment.service.CommentService;
import com.example.waggle.commons.component.DatabaseCleanUp;
import com.example.waggle.comment.dto.CommentViewDto;
import com.example.waggle.comment.dto.CommentWriteDto;
import com.example.waggle.board.story.dto.StorySimpleViewDto;
import com.example.waggle.board.story.dto.StoryWriteDto;
import com.example.waggle.member.dto.SignUpDto;
import com.example.waggle.commons.util.service.BoardType;
import com.example.waggle.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class CommentServiceTest {

    @Autowired
    private StoryService storyService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private CommentService commentService;
    @Autowired
    DatabaseCleanUp databaseCleanUp;


    SignUpDto signUpDto1;
    SignUpDto signUpDto2;


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

        signUpDto1 = SignUpDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();

        signUpDto2 = SignUpDto.builder()
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

    private void setBoardAndMember() {
        //member set
        memberService.signUp(signUpDto1);
        memberService.signUp(signUpDto2);

        //story set
        storyService.saveStory(storyWriteDto1);
        storyService.saveStory(storyWriteDto2);
    }

    @Test
    @WithMockCustomUser
    void saveComment() {
        //given
        setBoardAndMember();
        StorySimpleViewDto storySimpleViewDto = storyService.findAllStory().get(0);

        //when
        commentService.saveComment(storySimpleViewDto.getId(), commentWriteDto1, BoardType.STORY);
        List<CommentViewDto> comments = commentService.findComments(storySimpleViewDto.getId());
        //then
        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0).getContent()).isEqualTo("comment1");
        assertThat(comments.get(0).getUsername()).isEqualTo("member1");
    }

    @Test
    @WithMockCustomUser
    void editCommentV1() {
        //given
        setBoardAndMember();
        StorySimpleViewDto storySimpleViewDto = storyService.findAllStory().get(0);
        commentService.saveComment(storySimpleViewDto.getId(), commentWriteDto1, BoardType.STORY);
        List<CommentViewDto> comments = commentService.findComments(storySimpleViewDto.getId());
        List<CommentViewDto> editComments = new ArrayList<>();

        //when
        if (commentService.checkMember(comments.get(0))) {
            commentService.editComment(comments.get(0), commentWriteDto2);
             editComments = commentService.findComments(storySimpleViewDto.getId());
        }

        //then
        log.info("comment = {}", editComments.get(0).getContent());
        assertThat(editComments.get(0).getContent()).isEqualTo("comment2");
    }


    @Test
    @WithMockCustomUser
    @Rollback(value = false)
    void deleteComment() {
        //given
        setBoardAndMember();
        StorySimpleViewDto storySimpleViewDto = storyService.findAllStory().get(0);

        commentService.saveComment(storySimpleViewDto.getId(), commentWriteDto1, BoardType.STORY);
        List<CommentViewDto> comments = commentService.findComments(storySimpleViewDto.getId());

        //when
        log.info("boardId is {}", comments.get(0).getId());
        commentService.deleteComment(comments.get(0));
        List<CommentViewDto> deleteComments = commentService.findComments(storySimpleViewDto.getId());
        //then
        assertThat(deleteComments.size()).isEqualTo(0);

    }
}