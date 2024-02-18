package com.example.waggle.recommend.service;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.service.StoryCommandService;
import com.example.waggle.domain.board.story.service.StoryQueryService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.recommend.service.RecommendCommandService;
import com.example.waggle.domain.recommend.service.RecommendQueryService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.story.StoryRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    private MemberQueryService memberQueryService;
    @Autowired
    private RecommendCommandService recommendService;
    @Autowired
    private RecommendQueryService recommendQueryService;
    @Autowired
    DatabaseCleanUp databaseCleanUp;


    MemberRequest.AccessDto signUpDto1;
    MemberRequest.AccessDto signUpDto2;

    StoryRequest.Post storyWriteDto1;
    StoryRequest.Post storyWriteDto2;

    List<String> tags1 = new ArrayList<>();
    List<String> tags2 = new ArrayList<>();
    List<String> medias1 = new ArrayList<>();
    List<String> medias2 = new ArrayList<>();

    Member member;
    Member recommendMember;


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
                .password("12345678")
                .email("hi")
                .build();

        signUpDto2 = MemberRequest.AccessDto.builder()
                .password("12345678")
                .email("hoe")
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

    void setBoardAndMember() {

        //member set
        Long memberId = memberService.signUp(signUpDto1);
        Long recommendMemberId = memberService.signUp(signUpDto2);
        member = memberQueryService.getMemberById(memberId);
        recommendMember = memberQueryService.getMemberById(recommendMemberId);

        //story set
        storyService.createStoryByUsername(storyWriteDto1, null, member.getUsername());
    }

    @Test
    void recommendBoard() {
        //given
        setBoardAndMember();
        Story story = storyQueryService.getStories().get(0);

        //when
        recommendService.handleRecommendationByUsername(story.getId(), recommendMember.getUsername());
        Story storyByBoardId = storyQueryService.getStoryByBoardId(story.getId());
        int count = recommendQueryService.countRecommend(storyByBoardId.getId());

        //then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void cancelRecommendBoard() {
        //given
        setBoardAndMember();
        Story story = storyQueryService.getStories().get(0);
        recommendService.handleRecommendationByUsername(story.getId(), recommendMember.getUsername());
        recommendService.handleRecommendationByUsername(story.getId(), recommendMember.getUsername());

        //when
        Story storyByBoardId = storyQueryService.getStoryByBoardId(story.getId());
        boolean recommend = recommendQueryService.checkRecommend(storyByBoardId.getId(), member.getId());

        //then
        assertThat(recommend).isFalse();
    }

    @Test
    void cannot_recommend_Mine() {
        //given
        setBoardAndMember();
        Story story = storyQueryService.getStories().get(0);
        //then
        assertThatThrownBy(() -> recommendService.handleRecommendationByUsername(story.getId(), member.getUsername()));
    }

    @Test
    void story_remove_result_recommendEntity() {
        //given
        Long member1Id = memberService.signUp(signUpDto1);
        Long member2Id = memberService.signUp(signUpDto2);
        Member memberById = memberQueryService.getMemberById(member1Id);
        Member memberById1 = memberQueryService.getMemberById(member2Id);

        StoryRequest.Post request = StoryRequest.Post.builder()
                .content("hi")
                .build();

        storyService.createStoryByUsername(request, null, memberById.getUsername());
        Story story = storyQueryService.getStories().get(0);
        recommendService.handleRecommendationByUsername(story.getId(), memberById1.getUsername());
        //when
        storyService.deleteStoryByUsername(story.getId(), memberById.getUsername());
        int size = recommendQueryService.getRecommendingMembers(story.getId()).size();
        //then
        assertThat(size).isEqualTo(0);
    }

}