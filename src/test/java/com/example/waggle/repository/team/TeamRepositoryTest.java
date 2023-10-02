package com.example.waggle.repository.team;

import com.example.waggle.schedule.domain.Team;
import com.example.waggle.member.domain.TeamMember;
import com.example.waggle.member.dto.MemberDetailDto;
import com.example.waggle.member.dto.SignUpDto;
import com.example.waggle.schedule.dto.TeamDto;
import com.example.waggle.member.service.MemberService;
import com.example.waggle.schedule.service.TeamService;
import com.example.waggle.member.repository.TeamMemberRepository;
import com.example.waggle.schedule.repository.TeamRepository;
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

        MemberDetailDto savedMemberDetailDto = memberService.signUp(signUpDto, null);

        // Team 생성 (member 가입)
        TeamDto team = TeamDto.builder()
                .name("team").build();
        TeamDto savedTeam = teamService.createTeam(team, savedMemberDetailDto.getUsername());

        // when
        Team findTeam = teamRepository.findById(savedTeam.getId()).get();
        List<TeamMember> teamMembers = findTeam.getTeamMembers();

        // then
        assertThat(teamMembers.size()).isEqualTo(1);
        assertThat(teamMembers.get(0).getMember().getUsername()).isEqualTo(savedMemberDetailDto.getUsername());

    }
}