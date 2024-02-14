package com.example.waggle.board.story.service;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.service.StoryCommandService;
import com.example.waggle.domain.board.story.service.StoryQueryService;
import com.example.waggle.domain.hashtag.entity.Hashtag;
import com.example.waggle.domain.hashtag.service.HashtagQueryService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.story.StoryRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class StoryServiceTest {

    @Autowired
    private StoryQueryService storyService;
    @Autowired
    StoryCommandService storyCommandService;
    @Autowired
    private MemberCommandService memberService;
    @Autowired
    private MemberQueryService memberQueryService;
    @Autowired
    private HashtagQueryService hashtagQueryService;

    @Autowired
    DatabaseCleanUp databaseCleanUp;


    MemberRequest.AccessDto signUpDto1;
    MemberRequest.AccessDto signUpDto2;

    StoryRequest.Post storyWriteDto1;
    StoryRequest.Post storyWriteDto2;
    StoryRequest.Post storyWriteDto3;
    StoryRequest.Post storyWriteDto4;

    List<String> tags1 = new ArrayList<>();
    List<String> tags2 = new ArrayList<>();
    List<String> medias1 = new ArrayList<>();
    List<String> medias2 = new ArrayList<>();

    Pageable pageable = PageRequest.of(0, 3);

    Long memberId1;
    Long memberId2;


    @BeforeEach
    void setDto() {
        tags1.add("choco");
        tags1.add("poodle");
        tags2.add("poodle");

        medias1.add("media1");
        medias1.add("mediamedia1");
        medias2.add("media2");
        medias2.add("mediamedia2");


        signUpDto1 = MemberRequest.AccessDto.builder()
                .email("email1@naver.com")
                .password("password")
                .build();

        signUpDto2 = MemberRequest.AccessDto.builder()
                .email("email2@naver.com")
                .password("password")
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

        storyWriteDto3 = StoryRequest.Post.builder()
                .content("how can i do make he is happy?")
                .hashtags(tags2)
                .medias(medias2)
                .build();
        storyWriteDto4 = StoryRequest.Post.builder()
                .content("how can i do make he is happy?")
                .hashtags(tags2)
                .medias(medias2)
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
        Member member1 = memberQueryService.getMemberById(memberId1);
        Member member2 = memberQueryService.getMemberById(memberId2);

        //story set
        storyCommandService.createStoryByUsername(storyWriteDto1, null, member1.getUsername());
        storyCommandService.createStoryByUsername(storyWriteDto2, null, member2.getUsername());
    }


    @Test
    void findAll() {
        //given
        setBoardAndMember();

        //when
        List<Story> stories = storyService.getStories();

        //then
        assertThat(stories.size()).isEqualTo(2);
    }

    @Test
    void findAllStoryByMember() {
        //given
        setBoardAndMember();

        //when
        Page<Story> member1 = storyService.getPagedStoriesByMemberId(memberId1, pageable);
        //List<StorySimpleViewDto> user1 = storyService.findAllStoryByUsername("user1");

        //then
        assertThat(member1.getContent().size()).isEqualTo(2);
        //assertThat(user1.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    void findStoryViewByBoardId() {
        //given
        setBoardAndMember();

        //when
        List<Story> stories = storyService.getStories();
        Story storyByBoardId = storyService.getStoryByBoardId(stories.get(0).getId());

        //then
        assertThat(storyByBoardId.getBoardHashtags().size()).isEqualTo(2);
    }

    @Test
    @Transactional
    void changeStory() {
        //given
        setBoardAndMember();
        Long id = storyService.getStories().get(0).getId();
        List<String> tags = new ArrayList<>();
        tags.add("poodle");
        tags.add("cute");
        StoryRequest.Post editDto = StoryRequest.Post.builder()
                .content("edit edit edit")
                .hashtags(tags)
                .medias(medias2)
                .build();
        //when
//        boolean isSameUser = storyCommandService.validateMember(id);

        storyCommandService.updateStory(id, editDto, null, null);
        Story storyByBoardId = storyService.getStoryByBoardId(id);

        //then
//        assertThat(isSameUser).isTrue();
        assertThat(storyByBoardId.getContent()).isEqualTo("edit edit edit");
        assertThat(storyByBoardId.getBoardHashtags().size()).isEqualTo(2);
    }

    @Test
    void deleteStory() {
        //given
        setBoardAndMember();
        List<Story> stories = storyService.getStories();
        Story storyByBoardId = storyService.getStoryByBoardId(stories.get(0).getId());
        //when
        storyCommandService.deleteStory(storyByBoardId.getId());
        log.info("=========remove service ==============");
        List<Hashtag> allHashtags = hashtagQueryService.getAllHashtags();
        Page<Story> pagedStories = storyService.getPagedStories(pageable);
        //then
        assertThat(pagedStories.getContent().size()).isEqualTo(1);
        assertThat(allHashtags.size()).isEqualTo(2);
    }

}