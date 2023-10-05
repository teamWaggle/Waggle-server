package com.example.waggle.service.board;

import com.example.waggle.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.board.story.service.StoryService;
import com.example.waggle.commons.component.DatabaseCleanUp;
import com.example.waggle.board.story.dto.StorySummaryDto;
import com.example.waggle.board.story.dto.StoryDetailDto;
import com.example.waggle.board.story.dto.StoryWriteDto;
import com.example.waggle.member.dto.SignUpDto;
import com.example.waggle.member.service.MemberService;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class StoryServiceTest {

    @Autowired
    private StoryService storyService;
    @Autowired
    private MemberService memberService;
    @Autowired
    DatabaseCleanUp databaseCleanUp;

    SignUpDto signUpDto1;
    SignUpDto signUpDto2;

    StoryWriteDto storyWriteDto1;
    StoryWriteDto storyWriteDto2;
    StoryWriteDto storyWriteDto3;
    StoryWriteDto storyWriteDto4;

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

        storyWriteDto3 = StoryWriteDto.builder()
                .content("how can i do make he is happy?")
                .hashtags(tags2)
                .medias(medias2)
                .thumbnail("www.waggle")
                .build();
        storyWriteDto4 = StoryWriteDto.builder()
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

    private void setBoardAndMember() {
        //member set
        memberService.signUp(signUpDto1, null);
        memberService.signUp(signUpDto2, null);

        //story set
        storyService.createStory(storyWriteDto1);
        storyService.createStory(storyWriteDto2);
        storyService.createStory(storyWriteDto3);
        storyService.createStory(storyWriteDto4);
    }



    @Test
    @WithMockCustomUser
    void findAll() throws Exception {
        //given
        setBoardAndMember();

        //when
        List<StorySummaryDto> allStory = storyService.getStories();

        //then
        assertThat(allStory.size()).isEqualTo(2);

        log.info("success");
    }

    @Test
    @WithMockCustomUser
    void findAllStoryByMember() {
        //given
        setBoardAndMember();

        //when
        List<StorySummaryDto> allStoryByMember = storyService.getStoriesByUsername("member1");
        //List<StorySimpleViewDto> user1 = storyService.findAllStoryByUsername("user1");

        //then
        assertThat(allStoryByMember.size()).isEqualTo(2);
        //assertThat(user1.size()).isEqualTo(1);
    }

    @Test
    @WithMockCustomUser
    void findStoryViewByBoardId() {
        //given
        setBoardAndMember();

        //when
        List<StorySummaryDto> allStory = storyService.getStories();
        StoryDetailDto storyViewByBoardId = storyService.getStoryByBoardId(allStory.get(0).getId());

        //then
        assertThat(storyViewByBoardId.getUsername()).isEqualTo("member1");
        assertThat(storyViewByBoardId.getHashtags().size()).isEqualTo(2);
        assertThat(storyViewByBoardId.getMedias().get(0)).isEqualTo("media1");
    }

    @Test
    @WithMockCustomUser
    void changeStory() {
        //given
        setBoardAndMember();
        Long id = storyService.getStories().get(0).getId();
        List<String> tags = new ArrayList<>();
        tags.add("poodle");
        tags.add("cute");
        StoryWriteDto editDto = StoryWriteDto.builder()
                .id(id)
                .content("edit edit edit")
                .thumbnail("www.choco")
                .hashtags(tags)
                .medias(medias2)
                .build();
        //when
        boolean isSameUser = storyService.validateMember(id);
        storyService.updateStory(id, editDto);
        StoryDetailDto storyViewByBoardId = storyService.getStoryByBoardId(id);

        //then
        assertThat(isSameUser).isTrue();
        assertThat(storyViewByBoardId.getContent()).isEqualTo("edit edit edit");
        assertThat(storyViewByBoardId.getHashtags().size()).isEqualTo(2);
        assertThat(storyViewByBoardId.getMedias().get(0)).isEqualTo("media2");
    }

    @Test
    @WithMockCustomUser
    void deleteStory() {
        //given
        setBoardAndMember();
        List<StorySummaryDto> findStories = storyService.getStories();
        StoryDetailDto storyViewByBoardId = storyService.getStoryByBoardId(findStories.get(0).getId());
        //when
        storyService.deleteStory(storyViewByBoardId.getId());
        log.info("=========remove service ==============");
        //then
        List<StorySummaryDto> allStory = storyService.getStories();

        assertThat(allStory.size()).isEqualTo(1);
    }

    @Test
    @WithMockCustomUser
    void getStoriesByPaging() {

        setBoardAndMember();

        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(0, 2,sort);

        Page<StorySummaryDto> storiesPaging = storyService.getStoriesPaging(pageable);
        List<StorySummaryDto> content = storiesPaging.getContent();

        for (StorySummaryDto storySummaryDto : content) {
            log.info("storySummaryDto.getId() = {}" ,storySummaryDto.getId());
        }
    }
}