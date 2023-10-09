package com.example.waggle.service.board;

import com.example.waggle.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.board.story.service.StoryService;
import com.example.waggle.commons.component.DatabaseCleanUp;
import com.example.waggle.board.story.domain.Story;
import com.example.waggle.member.domain.Member;
import com.example.waggle.board.story.dto.StorySummaryDto;
import com.example.waggle.board.story.dto.StoryDetailDto;
import com.example.waggle.board.story.dto.StoryWriteDto;
import com.example.waggle.member.dto.SignUpDto;
import com.example.waggle.board.story.repository.StoryRepository;
import com.example.waggle.member.repository.MemberRepository;
import com.example.waggle.commons.util.service.BoardType;
import com.example.waggle.recommend.service.RecommendService;
import com.example.waggle.member.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RecommendServiceTest {

    @Autowired
    private StoryService storyService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private RecommendService recommendService;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private MemberRepository memberRepository;
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

    @Transactional
    void setBoardAndMember() throws IOException {

        //member set
        memberService.signUp(signUpDto1, null);
        memberService.signUp(signUpDto2, null);

        Member build = Member.builder().username("user1").password("password").build();
        memberRepository.save(build);

        Story iiii = Story.builder().member(build).content("iiii").build();
        storyRepository.save(iiii);

        //story set
        storyService.createStory(storyWriteDto1, new ArrayList<>(), null);
        //storyService.saveStory(storyWriteDto2);
    }

    @Test
    @WithMockCustomUser
    void recommendBoard() throws IOException {
        //given
        setBoardAndMember();
        StorySummaryDto storySummaryDto = storyService.getStories().get(0);

        //when
        recommendService.handleRecommendation(storySummaryDto.getId(), BoardType.STORY);
        StoryDetailDto storyViewByBoardId = storyService.getStoryByBoardId(storySummaryDto.getId());
        recommendService.checkRecommend(storyViewByBoardId);

        //then
        assertThat(storyViewByBoardId.getRecommendCount()).isEqualTo(1);
    }
    @Test
    @WithMockCustomUser
    void cancelRecommendBoard() throws IOException {
        //given
        setBoardAndMember();
        StorySummaryDto storySummaryDto = storyService.getStories().get(0);
        recommendService.handleRecommendation(storySummaryDto.getId(),BoardType.STORY);
        recommendService.handleRecommendation(storySummaryDto.getId(),BoardType.STORY);

        //when
        StoryDetailDto storyViewByBoardId = storyService.getStoryByBoardId(storySummaryDto.getId());
        recommendService.checkRecommend(storyViewByBoardId);

        //then
        assertThat(storyViewByBoardId.getRecommendCount()).isEqualTo(0);
    }
}