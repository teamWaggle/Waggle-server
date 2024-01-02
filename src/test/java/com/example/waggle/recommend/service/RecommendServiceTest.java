package com.example.waggle.recommend.service;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.repository.StoryRepository;
import com.example.waggle.domain.board.story.service.StoryCommandService;
import com.example.waggle.domain.board.story.service.StoryQueryService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.recommend.entity.Recommend;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.domain.recommend.service.RecommendCommandService;
import com.example.waggle.domain.recommend.service.RecommendQueryService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.domain.board.service.BoardType;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.story.StoryRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
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
    private RecommendRepository recommendRepository;
    @Autowired
    private StoryRepository storyRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    DatabaseCleanUp databaseCleanUp;


    MemberRequest.RegisterDto signUpDto1;
    MemberRequest.RegisterDto signUpDto2;

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

        signUpDto1 = MemberRequest.RegisterDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .email("hi")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();

        signUpDto2 = MemberRequest.RegisterDto.builder()
                .username("member2")
                .password("12345678")
                .nickname("닉네임2")
                .email("hoe")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();

        storyWriteDto1 = StoryRequest.Post.builder()
                .content("i love my choco")
                .hashtags(tags1)
                .build();

        storyWriteDto2 = StoryRequest.Post.builder()
                .content("how can i do make he is happy?")
                .hashtags(tags2)
                .build();



    }
    @AfterEach
    void clean() {
        databaseCleanUp.truncateAllEntity();
    }

    void setBoardAndMember() throws IOException {

        //member set
        memberService.signUp(signUpDto1);
        memberService.signUp(signUpDto2);

        Member build = Member.builder()
                .username("user1")
                .password("password")
                .email("email")
                .nickname("nickname")
                .build();
        memberRepository.save(build);

        Story iiii = Story.builder().member(build).content("iiii").build();
        storyRepository.save(iiii);

        //story set
        storyService.createStory(storyWriteDto1, null);
        //storyService.saveStory(storyWriteDto2);
    }

    @Test
    @WithMockCustomUser
    @Transactional
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
    @Transactional
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

    @Test
    @WithMockCustomUser
    @Transactional
    void cannot_recommend_Mine() throws IOException {
        //given
        memberService.signUp(signUpDto1);
        storyService.createStory(storyWriteDto1, null);
        Story story = storyQueryService.getStories().get(0);
        //then
        try {
            recommendService.handleRecommendation(story.getId(), BoardType.STORY);
        } catch (GeneralException ge) {
            log.info("ge = {}", ge);
        }
    }

    @Test
    @WithMockCustomUser
    @Transactional
    void story_remove_result_recommendEntity() throws IOException {
        //given
        memberService.signUp(signUpDto1);
        memberService.signUp(signUpDto2);
        Optional<Member> byUsername = memberRepository.findByUsername(signUpDto1.getUsername());

        StoryRequest.Post request = StoryRequest.Post.builder()
                .content("hi")
                .build();
        storyService.createStory(request, null);
        Story story = storyQueryService.getStories().get(0);
        Recommend build = Recommend.builder().member(byUsername.get()).board(story).build();
        recommendRepository.save(build);
        //when
        storyService.deleteStory(story.getId());
        List<Member> recommendingMembers = recommendQueryService.getRecommendingMembers(story.getId());
        //then
        assertThat(recommendingMembers.size()).isEqualTo(0);
    }
}