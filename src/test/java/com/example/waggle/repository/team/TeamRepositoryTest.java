package com.example.waggle.repository.team;

import com.example.waggle.domain.team.Team;
import com.example.waggle.domain.team.TeamMember;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.dto.team.TeamDto;
import com.example.waggle.service.member.MemberService;
import com.example.waggle.service.team.TeamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TeamRepositoryTest {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    TeamMemberRepository teamMemberRepository;

    @Autowired
    MemberService memberService;
    @Autowired
    TeamService teamService;

    @Test
    @Transactional
    void findByTeamMembers() {
        // given
        // Member 회원가입
        SignUpDto signUpDto = SignUpDto.builder()
                .username("member")
                .password("12345678")
                .nickname("닉네임")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();

        MemberDto savedMemberDto = memberService.signUp(signUpDto);

        // Team 생성 (member 가입)
        TeamDto team = TeamDto.builder()
                .name("team").build();
        TeamDto savedTeam = teamService.createTeamWithMember(team, savedMemberDto.getUsername());

        // when
        Team findTeam = teamRepository.findById(savedTeam.getId()).get();
        List<TeamMember> teamMembers = findTeam.getTeamMembers();

        // then
        assertThat(teamMembers.size()).isEqualTo(1);
        assertThat(teamMembers.get(0).getMember().getUsername()).isEqualTo(savedMemberDto.getUsername());

    }
}