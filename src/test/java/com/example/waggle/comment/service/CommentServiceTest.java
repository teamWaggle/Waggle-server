package com.example.waggle.comment.service;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.service.StoryCommandService;
import com.example.waggle.domain.board.story.service.StoryQueryService;
import com.example.waggle.domain.conversation.entity.Comment;
import com.example.waggle.domain.conversation.service.comment.CommentCommandService;
import com.example.waggle.domain.conversation.service.comment.CommentQueryService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.comment.CommentRequest.CommentCreateDto;
import com.example.waggle.web.dto.member.MemberRequest.MemberCredentialsDto;
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
class CommentServiceTest {

    @Autowired
    private StoryQueryService storyService;
    @Autowired
    private StoryCommandService storyCommandService;
    @Autowired
    private MemberCommandService memberService;
    @Autowired
    private MemberQueryService memberQueryService;
    @Autowired
    private CommentQueryService commentService;
    @Autowired
    private CommentCommandService commentCommandService;
    @Autowired
    DatabaseCleanUp databaseCleanUp;


    MemberCredentialsDto signUpDto1;
    MemberCredentialsDto signUpDto2;


    StoryCreateDto storyWriteDto1;
    StoryCreateDto storyWriteDto2;

    CommentCreateDto commentWriteDto1;
    CommentCreateDto commentWriteDto2;

    List<String> tags1 = new ArrayList<>();
    List<String> tags2 = new ArrayList<>();
    List<String> medias1 = new ArrayList<>();
    List<String> medias2 = new ArrayList<>();
    Long memberId1;
    Long memberId2;
    Member member1;
    Member member2;


    @BeforeEach
    void setDto() {
        tags1.add("choco");
        tags1.add("poodle");
        tags2.add("poodle");

        medias1.add("media1");
        medias1.add("mediamedia1");
        medias2.add("media2");
        medias2.add("mediamedia2");

        signUpDto1 = MemberCredentialsDto.builder()
                .email("ertyuio")
                .password("string")
                .build();

        signUpDto2 = MemberCredentialsDto.builder()
                .email("78347dj")
                .password("string")
                .build();

        storyWriteDto1 = StoryCreateDto.builder()
                .content("i love my choco")
                .hashtagList(tags1)
                .mediaList(medias1)
                .build();

        storyWriteDto2 = StoryCreateDto.builder()
                .content("how can i do make he is happy?")
                .hashtagList(tags2)
                .mediaList(medias2)
                .build();

        commentWriteDto1 = CommentCreateDto.builder()
                .content("comment1")
                .build();

        commentWriteDto2 = CommentCreateDto.builder()
                .content("comment2")
                .build();


    }

    @AfterEach
    void clean() {
        databaseCleanUp.truncateAllEntity();
    }

    private void setBoardAndMember() {
        //member set
        memberId1 = memberService.signUp(signUpDto1);
        memberId2 = memberService.signUp(signUpDto2);
        member1 = memberQueryService.getMemberById(memberId1);
        member2 = memberQueryService.getMemberById(memberId2);

        //story set
        storyCommandService.createStoryByUsername(storyWriteDto1, null, member1.getUsername());
        storyCommandService.createStoryByUsername(storyWriteDto2, null, member2.getUsername());
    }

    @Test
    void saveComment() {
        //given
        setBoardAndMember();
        Story story = storyService.getStories().get(0);

        //when
        commentCommandService.createCommentByUsername(story.getId(), commentWriteDto1, member1.getUsername());
        List<Comment> comments = commentService.getComments(story.getId());
        //then
        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0).getContent()).isEqualTo("comment1");
    }

    @Test
    void editCommentV1() {
        //given
        setBoardAndMember();
        Story story = storyService.getStories().get(0);
        commentCommandService.createCommentByUsername(story.getId(), commentWriteDto1, member1.getUsername());
        List<Comment> comments = commentService.getComments(story.getId());

        //when
        commentCommandService.updateCommentByUsername(comments.get(0).getId(), member1.getUsername(), commentWriteDto2);
        List<Comment> commentList = commentService.getComments(story.getId());

        //then
        assertThat(commentList.get(0).getContent()).isEqualTo("comment2");
    }


    @Test
    void deleteComment() {
        //given
        setBoardAndMember();
        Story story = storyService.getStories().get(0);

        //when
        commentCommandService.createCommentByUsername(story.getId(), commentWriteDto1, member1.getUsername());
        List<Comment> comments = commentService.getComments(story.getId());
        commentCommandService.deleteCommentByUsername(comments.get(0).getId(), member1.getUsername());
        List<Comment> commentList = commentService.getComments(story.getId());

        //then
        assertThat(commentList.size()).isEqualTo(0);

    }

    @Test
    void deleteStory() {
        //given
        setBoardAndMember();
        Story story = storyService.getStories().get(0);

        //when
        commentCommandService.createCommentByUsername(story.getId(), commentWriteDto1, member1.getUsername());
        storyCommandService.deleteStoryByUsername(story.getId(), member1.getUsername());

        List<Comment> comments = commentService.getComments(story.getId());
        //then
        assertThat(comments.size()).isEqualTo(0);

    }
}