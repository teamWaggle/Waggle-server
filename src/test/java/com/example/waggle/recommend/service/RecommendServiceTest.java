package com.example.waggle.recommend.service;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.repository.StoryRepository;
import com.example.waggle.domain.board.story.service.StoryCommandService;
import com.example.waggle.domain.board.story.service.StoryQueryService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.recommend.service.RecommendCommandService;
import com.example.waggle.domain.recommend.service.RecommendQueryService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.global.util.service.BoardType;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.story.StoryRequest;
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
    private StoryCommandService storyService;
    @Autowired
    private StoryQueryService storyQueryService;
    @Autowired
    private MemberCommandService memberService;
    @Autowired
    private RecommendCommandService recommendService;
    @Autowired
    private RecommendQueryService recommendQueryService;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    DatabaseCleanUp databaseCleanUp;


    MemberRequest.RegisterRequestDto signUpDto1;
    MemberRequest.RegisterRequestDto signUpDto2;

    StoryRequest.Post storyWriteDto1;
    StoryRequest.Post storyWriteDto2;

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
                .thumbnail("www.waggle")
                .build();

        storyWriteDto2 = StoryRequest.Post.builder()
                .content("how can i do make he is happy?")
                .hashtags(tags2)
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
        Story story = storyQueryService.getStories().get(0);

        //when
        recommendService.handleRecommendation(story.getId(), BoardType.STORY);
        Story storyByBoardId = storyQueryService.getStoryByBoardId(story.getId());
        int count = recommendQueryService.countRecommend(storyByBoardId.getId());

        //then
        assertThat(count).isEqualTo(1);
    }
    @Test
    @WithMockCustomUser
    void cancelRecommendBoard() throws IOException {
        //given
        setBoardAndMember();
        Story story = storyQueryService.getStories().get(0);
        recommendService.handleRecommendation(story.getId(),BoardType.STORY);
        recommendService.handleRecommendation(story.getId(),BoardType.STORY);

        //when
        Story storyByBoardId = storyQueryService.getStoryByBoardId(story.getId());
        boolean recommend = recommendQueryService.checkRecommend(storyByBoardId.getId(), storyByBoardId.getMember().getUsername());

        //then
        assertThat(recommend).isFalse();
    }
}