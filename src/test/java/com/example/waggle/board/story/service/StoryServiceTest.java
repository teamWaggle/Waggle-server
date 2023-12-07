package com.example.waggle.board.story.service;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.service.StoryCommandService;
import com.example.waggle.domain.board.story.service.StoryQueryService;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
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
import org.springframework.data.domain.Sort;

import java.io.IOException;
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
    DatabaseCleanUp databaseCleanUp;

    MemberRequest.RegisterRequestDto signUpDto1;
    MemberRequest.RegisterRequestDto signUpDto2;

    StoryRequest.Post storyWriteDto1;
    StoryRequest.Post storyWriteDto2;
    StoryRequest.Post storyWriteDto3;
    StoryRequest.Post storyWriteDto4;

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

        storyWriteDto1 = StoryRequest.Post.builder()
                .content("i love my choco")
                .hashtags(tags1)
                .medias(medias1)
                .thumbnail("www.waggle")
                .build();

        storyWriteDto2 = StoryRequest.Post.builder()
                .content("how can i do make he is happy?")
                .hashtags(tags2)
                .medias(medias2)
                .thumbnail("www.waggle")
                .build();

        storyWriteDto3 = StoryRequest.Post.builder()
                .content("how can i do make he is happy?")
                .hashtags(tags2)
                .medias(medias2)
                .thumbnail("www.waggle")
                .build();
        storyWriteDto4 = StoryRequest.Post.builder()
                .content("how can i do make he is happy?")
                .hashtags(tags2)
                .medias(medias2)
                .thumbnail("www.waggle")
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
    void findAll() throws Exception {
        //given
        setBoardAndMember();

        //when
        List<Story> stories = storyService.getStories();

        //then
        assertThat(stories.size()).isEqualTo(2);
    }

    @Test
    @WithMockCustomUser
    void findAllStoryByMember() throws IOException {
        //given
        setBoardAndMember();

        //when
        Pageable pageable = PageRequest.of(0, 3);
        Page<Story> member1 = storyService.getPagedStoriesByUsername("member1", pageable);
        //List<StorySimpleViewDto> user1 = storyService.findAllStoryByUsername("user1");

        //then
        assertThat(member1.getContent().size()).isEqualTo(2);
        //assertThat(user1.size()).isEqualTo(1);
    }

    @Test
    @WithMockCustomUser
    void findStoryViewByBoardId() throws IOException {
        //given
        setBoardAndMember();

        //when
        List<Story> stories = storyService.getStories();
        Story storyByBoardId = storyService.getStoryByBoardId(stories.get(0).getId());

        //then
        assertThat(storyByBoardId.getMember().getUsername()).isEqualTo("member1");
        assertThat(storyByBoardId.getBoardHashtags().size()).isEqualTo(2);
    }

    @Test
    @WithMockCustomUser
    void changeStory() throws IOException {
        //given
        setBoardAndMember();
        Long id = storyService.getStories().get(0).getId();
        List<String> tags = new ArrayList<>();
        tags.add("poodle");
        tags.add("cute");
        StoryRequest.Post editDto = StoryRequest.Post.builder()
                .id(id)
                .content("edit edit edit")
                .thumbnail("www.choco")
                .hashtags(tags)
                .medias(medias2)
                .build();
        //when
//        boolean isSameUser = storyCommandService.validateMember(id);
        storyCommandService.updateStory(id, editDto, new ArrayList<>(), null);
        Story storyByBoardId = storyService.getStoryByBoardId(id);

        //then
//        assertThat(isSameUser).isTrue();
        assertThat(storyByBoardId.getContent()).isEqualTo("edit edit edit");
        assertThat(storyByBoardId.getBoardHashtags().size()).isEqualTo(2);
    }

    @Test
    @WithMockCustomUser
    void deleteStory() throws IOException {
        //given
        setBoardAndMember();
        List<Story> stories = storyService.getStories();
        Story storyByBoardId = storyService.getStoryByBoardId(stories.get(0).getId());
        //when
        storyCommandService.deleteStory(storyByBoardId.getId());
        log.info("=========remove service ==============");
        //then
        assertThat(stories.size()).isEqualTo(1);
    }

    @Test
    @WithMockCustomUser
    void getStoriesByPaging() throws IOException {

        setBoardAndMember();

        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(0, 2,sort);

        Page<Story> pagedStories = storyService.getPagedStories(pageable);
        List<Story> content = pagedStories.getContent();

        for (Story story : content) {
            log.info("storySummaryDto.getId() = {}" ,story.getId());
        }
    }
}