package com.example.waggle.follow.service;

import com.example.waggle.domain.follow.entity.Follow;
import com.example.waggle.domain.follow.service.FollowCommandService;
import com.example.waggle.domain.follow.service.FollowQueryService;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.global.exception.handler.FollowHandler;
import com.example.waggle.global.util.SecurityUtil;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.web.dto.member.MemberRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FollowServiceTest {
    @Autowired
    FollowQueryService followQueryService;
    @Autowired
    FollowCommandService followCommandService;
    @Autowired
    MemberCommandService memberCommandService;
    @Autowired
    DatabaseCleanUp databaseCleanUp;

    MemberRequest.RegisterDto signUpDto1;
    MemberRequest.RegisterDto signUpDto2;
    MemberRequest.RegisterDto signUpDto3;
    MemberRequest.RegisterDto signUpDto4;


    @AfterEach
    void afterEach() {
        databaseCleanUp.truncateAllEntity();
    }

    void setUp() {
        signUpDto1 = MemberRequest.RegisterDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .email("wjdgks3264@naver.com")
                .build();

        signUpDto2 = MemberRequest.RegisterDto.builder()
                .username("member2")
                .password("12345678")
                .nickname("닉네임2")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .email("wjdgks2972@naver.com")
                .build();

        signUpDto3 = MemberRequest.RegisterDto.builder()
                .username("member3")
                .password("12345678")
                .nickname("닉네임3")
                .address("서울시 광진구")
                .email("wjdgks@naver.com")
                .phone("010-1234-5678")
                .build();

        signUpDto4 = MemberRequest.RegisterDto.builder()
                .username("member4")
                .password("12345678")
                .nickname("닉네임4")
                .address("서울시 광진구")
                .email("hi@naver.com")
                .phone("010-1234-5678")
                .build();

        memberCommandService.signUp(signUpDto1);
        memberCommandService.signUp(signUpDto2);
        memberCommandService.signUp(signUpDto3);
        memberCommandService.signUp(signUpDto4);

    }

    @Test
    @WithMockCustomUser
    @Transactional
    void follow() {
        //given
        setUp();
        //when
        followCommandService.follow("member1", "member2");
        List<Follow> followingsByUser = followQueryService.getFollowings(SecurityUtil.getCurrentUsername());
        //then
        assertThat(followingsByUser.get(0).getToMember().getUsername()).isEqualTo("member2");
    }

    @Test
    @WithMockCustomUser
    @Transactional
    void unfollow() {
        //given
        setUp();
        //when
        Long followId = followCommandService.follow("member1", "member2");
        followCommandService.follow("member1", "member3");
        followCommandService.unFollow("member1", "member2");
        List<Follow> followingsByUser = followQueryService.getFollowings(SecurityUtil.getCurrentUsername());
        //then
        assertThat(followingsByUser.get(0).getFromMember().getUsername()).isEqualTo("member1");
        assertThat(followingsByUser.get(0).getToMember().getUsername()).isEqualTo("member3");
    }

    @Test
    @WithMockCustomUser
    @Transactional
    void follow_exception() {
        //given
        setUp();
        //when
        followCommandService.follow("member1", "member2");
        //then
        Assertions.assertThrows(FollowHandler.class, () -> followCommandService.follow("member1", "member2"));
    }

    @Test
    @WithMockCustomUser
    @Transactional
    void unfollow_exception() {
        //given
        setUp();
        //when
        Long followId = followCommandService.follow("member1", "member2");
        followCommandService.follow("member1", "member3");
        followCommandService.unFollow("member1", "member2");
        //then
        Assertions.assertThrows(FollowHandler.class, () -> followCommandService.unFollow("member1", "member2"));
    }
}
