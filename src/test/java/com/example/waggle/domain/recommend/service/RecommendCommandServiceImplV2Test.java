package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.board.story.service.StoryCommandService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.member.service.RedisService;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.member.MemberRequest.MemberCredentialsDto;
import com.example.waggle.web.dto.story.StoryRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        redisService.clearRecommend();
        redisService.clearRecommendCnt();
        redisService.clearInitRecommend();
        memberList.clear();
        memberIdList.clear();
        boardIdList.clear();
    }

    @Test
    @DisplayName("레디스를 통해 대량 좋아요 요청 처리하기.")
    void recommendByRedis() {
//        recommendSyncService.initRecommendationInRedis(memberList.get(0));

        for (int i = 0; i < 500; i++) {
            System.out.println("i = " + i);
            recommendCommandService.handleRecommendation(boardIdList.get(3), memberList.get(0));
            recommendCommandService.handleRecommendation(boardIdList.get(2), memberList.get(0));
            recommendCommandService.handleRecommendation(boardIdList.get(1), memberList.get(0));
            List<Long> recommendList = redisService.getRecommendedBoardList(memberIdList.get(0));
            recommendList.forEach(recommend -> log.info("recommend board  : {}", recommend));
        }
    }

//    @Test
//    @DisplayName("관계형 데이터베이스를 통해 대량 좋아요 요청 처리하기.")
//    void recommendByRDB() {
//
//        for (int i = 0; i < 500; i++) {
//            log.info("i = {}", i);
//            recommendCommandService.handleRecommendation(boardIdList.get(3), memberList.get(0));
//            recommendCommandService.handleRecommendation(boardIdList.get(2), memberList.get(0));
//            recommendCommandService.handleRecommendation(boardIdList.get(2), memberList.get(0));
//            List<Recommend> all = recommendRepository.findAll();
//            all.forEach(recommend -> log.info("recommend board : {}", recommend));
//        }
//    }

    @Test
    @DisplayName("좋아요를 누르고 취소하며 그때마다 redis내 데이터와 비교확인합니다.")
    void getRecommendByRedis() {
        //member 0,1,2 login -> init recommend
//        recommendSyncService.initRecommendationInRedis(memberList.get(0));
//        recommendSyncService.initRecommendationInRedis(memberList.get(1));
//        recommendSyncService.initRecommendationInRedis(memberList.get(2));

        for (int i = 0; i < 5; i++) {
            log.info("count = {}", i);
            makeRecommend();
            cancleRecommend();
        }
    }

//    @Test
//    @DisplayName("좋아요를 만들거나 취소하며 그때마다 rdb내 데이터와 비교확인합니다.")
//    void getRecommendByRDB() {
//        for (int i = 0; i < 3; i++) {
//            log.info("count = {}", i);
//            makeRecommend();
//            cancleRecommend();
//        }
//    }

    @Test
    @DisplayName("좋아요를 누른 뒤, redis로부터 좋아요 개수를 확인합니다.")
    void getRecommendCntByRedis() {
        //member 0,1,2 login -> init recommend
//        recommendSyncService.initRecommendationInRedis(memberList.get(0));
//        recommendSyncService.initRecommendationInRedis(memberList.get(1));
//        recommendSyncService.initRecommendationInRedis(memberList.get(2));
        for (Long boardId : boardIdList) {
            recommendCommandService.handleRecommendation(boardId, memberList.get(0));
            recommendCommandService.handleRecommendation(boardId, memberList.get(1));
            recommendCommandService.handleRecommendation(boardId, memberList.get(2));
        }
        for (int i = 0; i < 10; i++) {
            log.info("count = {}", i);
            for (Long boardId : boardIdList) {
                assertThat(recommendQueryService.countRecommend(boardId)).isEqualTo(3);
            }
        }
    }

//    @Test
//    @DisplayName("redis 전체 플로우 확인")
//    void recommendEntireFlowUsingRedis() {
//        recommendSyncService.initRecommendationInRedis(memberList.get(0));
//        recommendSyncService.initRecommendationInRedis(memberList.get(1));
//        recommendSyncService.initRecommendationInRedis(memberList.get(2));
//
//        makeRecommend();
//
//        recommendSyncService.syncRecommendation();
//
//        assertThat(recommendRepository.findAll().size()).isEqualTo(30);
//
//        recommendSyncService.initRecommendationInRedis(memberList.get(0));
//        recommendSyncService.initRecommendationInRedis(memberList.get(1));
//        recommendSyncService.initRecommendationInRedis(memberList.get(2));
//
//        assertThat(redisService.getRecommendedBoardList(memberList.get(0).getId()).size()).isEqualTo(10);
//
//        for (Long boardId : boardIdList) {
//            assertThat(recommendQueryService.countRecommend(boardId)).isEqualTo(3);
//            assertThat(recommendQueryService.checkRecommend(boardId, memberList.get(0).getUsername())).isTrue();
//            assertThat(recommendQueryService.checkRecommend(boardId, memberList.get(1).getUsername())).isTrue();
//            assertThat(recommendQueryService.checkRecommend(boardId, memberList.get(2).getUsername())).isTrue();
//        }
//    }

    private void cancleRecommend() {
        for (Long boardId : boardIdList) {
            recommendCommandService.handleRecommendation(boardId, memberList.get(0));
            recommendCommandService.handleRecommendation(boardId, memberList.get(1));
            recommendCommandService.handleRecommendation(boardId, memberList.get(2));
        }
        for (Long boardId : boardIdList) {
            assertThat(recommendQueryService.checkRecommend(boardId, memberList.get(0).getUsername())).isFalse();
            assertThat(recommendQueryService.checkRecommend(boardId, memberList.get(1).getUsername())).isFalse();
            assertThat(recommendQueryService.checkRecommend(boardId, memberList.get(2).getUsername())).isFalse();
        }
    }

    private void makeRecommend() {
        for (Long boardId : boardIdList) {
            recommendCommandService.handleRecommendation(boardId, memberList.get(0));
            recommendCommandService.handleRecommendation(boardId, memberList.get(1));
            recommendCommandService.handleRecommendation(boardId, memberList.get(2));
        }
        for (Long boardId : boardIdList) {
            assertThat(recommendQueryService.checkRecommend(boardId, memberList.get(0).getUsername())).isTrue();
            assertThat(recommendQueryService.checkRecommend(boardId, memberList.get(1).getUsername())).isTrue();
            assertThat(recommendQueryService.checkRecommend(boardId, memberList.get(2).getUsername())).isTrue();
        }
    }


}