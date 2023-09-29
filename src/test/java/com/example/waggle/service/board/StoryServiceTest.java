package com.example.waggle.service.board;

import com.example.waggle.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.board.story.service.StoryService;
import com.example.waggle.commons.component.DatabaseCleanUp;
import com.example.waggle.board.story.dto.StorySimpleViewDto;
import com.example.waggle.board.story.dto.StoryViewDto;
import com.example.waggle.board.story.dto.StoryWriteDto;
import com.example.waggle.member.dto.SignUpDto;
import com.example.waggle.member.service.MemberService;
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
    void findAll() throws Exception {
        //given
        setBoardAndMember();

        //when
        List<StorySimpleViewDto> allStory = storyService.findAllStory();

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
        List<StorySimpleViewDto> allStoryByMember = storyService.findAllStoryByUsername("member1");
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
        List<StorySimpleViewDto> allStory = storyService.findAllStory();
        StoryViewDto storyViewByBoardId = storyService.findStoryViewByBoardId(allStory.get(0).getId());

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
        Long id = storyService.findAllStory().get(0).getId();
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
        boolean isSameUser = storyService.checkMember(id);
        storyService.changeStory(editDto);
        StoryViewDto storyViewByBoardId = storyService.findStoryViewByBoardId(id);

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
        List<StorySimpleViewDto> findStories = storyService.findAllStory();
        StoryViewDto storyViewByBoardId = storyService.findStoryViewByBoardId(findStories.get(0).getId());
        //when
        storyService.removeStory(storyViewByBoardId);
        log.info("=========remove service ==============");
        //then
        List<StorySimpleViewDto> allStory = storyService.findAllStory();

        assertThat(allStory.size()).isEqualTo(1);
    }
}