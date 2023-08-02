package com.example.waggle.service.board;

import com.example.waggle.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.component.DatabaseCleanUp;
import com.example.waggle.domain.board.Media;
import com.example.waggle.dto.board.story.StoryViewDto;
import com.example.waggle.dto.board.story.StorySimpleViewDto;
import com.example.waggle.dto.board.story.StoryWriteDto;
import com.example.waggle.dto.member.SignInDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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
    SignInDto signInDto;

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
    //@WithMockCustomUser
    @WithMockUser(username = "user1", password = "12345678")
    //@Rollback(value = false)
    void findAll() throws Exception {
        //given
        //setBoardAndMember();

        //when
        List<StorySimpleViewDto> allStory = storyService.findAllStory();

        //then
        assertThat(allStory.size()).isEqualTo(5);

        log.info("success");
    }

    @Test
    @WithMockCustomUser
    //Rollback(value = false)
    void findAllStoryByMember() {
        //given
        setBoardAndMember();

        //when
        List<StorySimpleViewDto> allStoryByMember = storyService.findAllMemberStories("member1");
        List<StorySimpleViewDto> user1 = storyService.findAllMemberStories("user1");

        //then
        assertThat(allStoryByMember.size()).isEqualTo(2);
        assertThat(user1.size()).isEqualTo(1);
    }

    @Test
    @WithMockCustomUser
    //@Rollback(value = false)
    void findStoryViewByBoardId() {
        //given
        setBoardAndMember();

        //when
        StoryViewDto storyViewByBoardId = storyService.findStoryViewByBoardId(6L);

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
        List<String> tags = new ArrayList<>();
        tags.add("poodle");
        tags.add("cute");
        StoryWriteDto editDto = StoryWriteDto.builder()
                .id(6L)
                .content("edit edit edit")
                .thumbnail("www.choco")
                .hashtags(tags)
                .medias(medias2)
                .build();
        //when
        boolean isSameUser = storyService.checkMember(6L);
        storyService.changeStory(editDto);
        StoryViewDto storyViewByBoardId = storyService.findStoryViewByBoardId(6L);

        //then
        assertThat(isSameUser).isTrue();
        assertThat(storyViewByBoardId.getContent()).isEqualTo("edit edit edit");
        assertThat(storyViewByBoardId.getHashtags().size()).isEqualTo(2);
        assertThat(storyViewByBoardId.getMedias().get(0)).isEqualTo("media2");
    }

    @Test
    @WithMockCustomUser
    //@Rollback(value = false)
    void deleteStory() {
        //given
        setBoardAndMember();
        StoryViewDto storyViewByBoardId = storyService.findStoryViewByBoardId(6L);
        //when
        storyService.removeStory(storyViewByBoardId);
        log.info("=========remove service ==============");
        //then
        List<StorySimpleViewDto> allStory = storyService.findAllStory();

        assertThat(allStory.size()).isEqualTo(6);
    }
}