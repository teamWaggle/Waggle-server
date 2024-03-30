package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.board.story.service.StoryCommandService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.member.service.RedisService;
import com.example.waggle.domain.recommend.entity.Recommend;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.member.MemberRequest.MemberCredentialsDto;
import com.example.waggle.web.dto.story.StoryRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
class RecommendCommandServiceImplV2Test {

    @Autowired
    MemberCommandService memberCommandService;
    @Autowired
    MemberQueryService memberQueryService;
    @Autowired
    StoryCommandService storyCommandService;
    @Autowired
    RecommendCommandService recommendCommandService;
    @Autowired
    RecommendQueryService recommendQueryService;
    @Autowired
    RecommendSyncService recommendSyncService;
    @Autowired
    RecommendRepository recommendRepository;
    @Autowired
    RedisService redisService;
    @Autowired
    DatabaseCleanUp databaseCleanUp;

    List<Long> memberIdList = new ArrayList<>();
    List<Member> memberList = new ArrayList<>();
    List<Long> boardIdList = new ArrayList<>();

    @BeforeEach
    void init() {
        for (int i = 0; i < 5; i++) {
            MemberCredentialsDto member = MemberCredentialsDto.builder()
                    .email("string" + String.valueOf(i) + "@naver.com")
                    .password("string")
                    .build();
            memberIdList.add(memberCommandService.signUp(member));
        }
        for (Long memberId : memberIdList) {
            memberList.add(memberQueryService.getMemberById(memberId));
        }
        for (int i = 0; i < 10; i++) {
            StoryRequest hello = StoryRequest.builder()
                    .content("hello")
                    .hashtagList(new ArrayList<>())
                    .mediaList(new ArrayList<>())
                    .build();
            boardIdList.add(storyCommandService.createStory(hello, memberList.get(i % memberList.size())));
        }

    }

    @AfterEach
    void cleanUp() {
        databaseCleanUp.truncateAllEntity();
        memberList.clear();
        memberIdList.clear();
        boardIdList.clear();
    }

    @Test
    void syncRecommendByRedis() {
//        recommendSyncService.initRecommendationInRedis(memberList.get(0));

        for (int i = 0; i < 500; i++) {
            System.out.println("i = " + i);
            recommendCommandService.handleRecommendation(boardIdList.get(3), memberList.get(0));
            recommendCommandService.handleRecommendation(boardIdList.get(2), memberList.get(0));
            recommendCommandService.handleRecommendation(boardIdList.get(1), memberList.get(0));
            List<Long> recommendList = redisService.getRecommendList(memberIdList.get(0));
            recommendList.forEach(recommend -> log.info("recommend board  : {}", recommend));
        }
    }

    @Test
    void syncRecommendByRDB() {

        for (int i = 0; i < 500; i++) {
            log.info("i = {}", i);
            recommendCommandService.handleRecommendation(boardIdList.get(3), memberList.get(0));
            recommendCommandService.handleRecommendation(boardIdList.get(2), memberList.get(0));
            recommendCommandService.handleRecommendation(boardIdList.get(2), memberList.get(0));
            List<Recommend> all = recommendRepository.findAll();
            all.forEach(recommend -> log.info("recommend board : {}", recommend));
        }
    }
}