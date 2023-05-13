package com.example.waggle.service.member;

import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.dto.member.TeamDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class TeamServiceTest {
    @Autowired MemberService memberService;
    @Autowired TeamService teamService;

    @Test
    @Rollback(value = false)
    public void createTeamWithMemberTest() {
        SignUpDto signUpDto = SignUpDto.builder()
                .username("user")
                .password("12345678")
                .nickname("닉네임")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        MemberDto savedMemberDto = memberService.signUp(signUpDto);

        TeamDto teamA = TeamDto.builder()
                .name("teamA").build();

        teamService.createTeamWithMember(teamA, savedMemberDto);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void createThenAddMemberTest() {
        // Member 회원가입
        SignUpDto signUpDto1 = SignUpDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        SignUpDto signUpDto2 = SignUpDto.builder()
                .username("member2")
                .password("12345678")
                .nickname("닉네임2")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();

        MemberDto savedMemberDto1 = memberService.signUp(signUpDto1);
        MemberDto savedMemberDto2 = memberService.signUp(signUpDto2);

        // Team 생성 (member1 가입)
        TeamDto team = TeamDto.builder()
                .name("team").build();

        TeamDto savedTeamDto1 = teamService.createTeamWithMember(team, savedMemberDto1);

        // member2 가입
        TeamDto savedTeamDto2 = teamService.addMember(savedTeamDto1, savedMemberDto2.getUsername());

        // team 테이블의 전체 개수 1개
        assertThat(teamService.findAllTeam().size()).isEqualTo(1);

        TeamDto findTeam = teamService.findByTeamId(1L).get();
        assertThat(findTeam.getTeamMembers().size()).isEqualTo(2);
        assertThat(findTeam.getName()).isEqualTo("team");
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void createThenAddMemberThenRemoveTeamTest() {
        // Member 회원가입
        SignUpDto signUpDto1 = SignUpDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        SignUpDto signUpDto2 = SignUpDto.builder()
                .username("member2")
                .password("12345678")
                .nickname("닉네임2")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();

        MemberDto savedMemberDto1 = memberService.signUp(signUpDto1);
        MemberDto savedMemberDto2 = memberService.signUp(signUpDto2);

        // Team 생성 (member1 가입)
        TeamDto team = TeamDto.builder()
                .name("team").build();

        TeamDto savedTeamDto1 = teamService.createTeamWithMember(team, savedMemberDto1);

        // member2 가입
        TeamDto savedTeamDto2 = teamService.addMember(savedTeamDto1, savedMemberDto2.getUsername());

        // team 삭제
        TeamDto findTeam = teamService.findByTeamId(savedTeamDto2.getId()).get();
        teamService.removeTeam(findTeam);
        assertThat(teamService.findAllTeam().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void findTeamMembersTest() {
        // Member 회원가입
        SignUpDto signUpDto1 = SignUpDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        SignUpDto signUpDto2 = SignUpDto.builder()
                .username("member2")
                .password("12345678")
                .nickname("닉네임2")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();

        MemberDto savedMemberDto1 = memberService.signUp(signUpDto1);
        MemberDto savedMemberDto2 = memberService.signUp(signUpDto2);

        // Team 생성 (member1 가입)
        TeamDto team = TeamDto.builder()
                .name("team").build();

        TeamDto savedTeamDto1 = teamService.createTeamWithMember(team, savedMemberDto1);

        // member2 가입
        TeamDto savedTeamDto2 = teamService.addMember(savedTeamDto1, savedMemberDto2.getUsername());

        // team 삭제
        List<MemberDto> teamMembers = teamService.findTeamMembers(savedTeamDto2.getId());
        assertThat(teamMembers.size()).isEqualTo(2);

        assertThat(teamMembers).contains(savedMemberDto1, savedMemberDto2); // MemberDto에 @EqualsAndHashCode
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void removeMemberTest() {
        // Member 회원가입
        SignUpDto signUpDto1 = SignUpDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        SignUpDto signUpDto2 = SignUpDto.builder()
                .username("member2")
                .password("12345678")
                .nickname("닉네임2")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();

        MemberDto savedMemberDto1 = memberService.signUp(signUpDto1);
        MemberDto savedMemberDto2 = memberService.signUp(signUpDto2);

        // Team 생성 (member1 가입)
        TeamDto team = TeamDto.builder()
                .name("team").build();

        TeamDto savedTeamDto1 = teamService.createTeamWithMember(team, savedMemberDto1);

        // member2 가입
        TeamDto savedTeamDto2 = teamService.addMember(savedTeamDto1, savedMemberDto2.getUsername());

        // member1 삭제 -> team에 속한 전체 member 수는 1이 되어야 함
        teamService.removeMember(savedTeamDto2, savedMemberDto1.getUsername());

        TeamDto teamDto = teamService.findByTeamId(savedTeamDto2.getId()).get();

        assertThat(teamDto.getTeamMembers().size()).isEqualTo(1);
    }


}