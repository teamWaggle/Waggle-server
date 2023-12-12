package com.example.waggle.follow.service;

import com.example.waggle.domain.follow.entity.Follow;
import com.example.waggle.domain.follow.service.FollowCommandService;
import com.example.waggle.domain.follow.service.FollowQueryService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
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
    MemberRepository memberRepository;

    void setUp() {
        Member member1 = Member.builder().nickname("1").password("12345678").username("member1").build();
        Member member2 = Member.builder().nickname("2").password("12345678").username("member2").build();
        Member member3 = Member.builder().nickname("3").password("12345678").username("member3").build();
        Member member4 = Member.builder().nickname("4").password("12345678").username("member4").build();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
    }

    @Test
    @Transactional
    @WithMockCustomUser
    void follow() {
        //given
        setUp();
        //when
        followCommandService.follow("member2");
        List<Follow> followingsByUser = followQueryService.getFollowingsByUser();
        //then
        assertThat(followingsByUser.get(0).getFromUser().getUsername()).isEqualTo("member1");
        assertThat(followingsByUser.get(0).getToUser().getUsername()).isEqualTo("member2");
    }

}
