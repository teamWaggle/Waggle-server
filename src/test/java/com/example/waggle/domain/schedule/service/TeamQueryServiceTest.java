package com.example.waggle.domain.schedule.service;

<<<<<<< HEAD
import static org.assertj.core.api.Assertions.assertThat;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.schedule.domain.Team;
import com.example.waggle.domain.schedule.domain.TeamMember;
import com.example.waggle.domain.schedule.repository.TeamRepository;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
import java.util.List;
=======
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.schedule.entity.Team;
import com.example.waggle.domain.schedule.entity.TeamMember;
import com.example.waggle.domain.schedule.repository.TeamMemberRepository;
import com.example.waggle.domain.schedule.repository.TeamRepository;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
>>>>>>> refactor/87
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD
=======
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

>>>>>>> refactor/87
@WithMockCustomUser
@Transactional
@SpringBootTest
class TeamQueryServiceTest {

    @Autowired
    TeamQueryService teamQueryService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
<<<<<<< HEAD
=======
    @Autowired
    TeamMemberRepository teamMemberRepository;
>>>>>>> refactor/87

    private Member member1;
    private Member member2;
    private Team team1;
    private Team team2;


    @BeforeEach
    void setUp() {
        // Setup member
        member1 = Member.builder()
                .username("member1")
                .password("12345678")
<<<<<<< HEAD
=======
                .nickname("hi")
                .email("wjdgks@naver.com")
>>>>>>> refactor/87
                .build();
        memberRepository.save(member1);

        member2 = Member.builder()
                .username("member2")
                .password("12345678")
<<<<<<< HEAD
=======
                .nickname("hoe")
                .email("wjdgks3264@naver.com")
>>>>>>> refactor/87
                .build();
        memberRepository.save(member2);

        // Setup team
        team1 = Team.builder()
                .name("team1")
                .description("team1 description")
                .leader(member1)
                .maxTeamSize(4)
<<<<<<< HEAD
=======
                .colorScheme("red")
>>>>>>> refactor/87
                .build();
        teamRepository.save(team1);

        team2 = Team.builder()
                .name("team2")
                .description("team2 description")
                .leader(member1)
                .maxTeamSize(4)
<<<<<<< HEAD
=======
                .colorScheme("orange")
>>>>>>> refactor/87
                .build();
        teamRepository.save(team2);

        // Setup teamMember
<<<<<<< HEAD
        TeamMember teamMember1 = TeamMember.builder().build();
        teamMember1.addTeamMember(team1, member1);

        TeamMember teamMember2 = TeamMember.builder().build();
        teamMember2.addTeamMember(team2, member1);

        TeamMember teamMember3 = TeamMember.builder().build();
        teamMember3.addTeamMember(team1, member2);
=======
        addMemberToTeam(team1, member1);
        addMemberToTeam(team2, member1);
        addMemberToTeam(team1, member2);
    }

    private void addMemberToTeam(Team team, Member member) {
        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .member(member)
                .build();
        teamMember.addTeamMember(team, member);
        teamMemberRepository.save(teamMember);
>>>>>>> refactor/87
    }

    @Test
    void getTeamsByUsername() {
        // when
        Pageable pageable = PageRequest.of(0, 5);
        List<Team> teams = teamQueryService.getTeamsByUsername(member1.getUsername(), pageable).getContent();

        // then
        assertThat(teams.size()).isEqualTo(2);
        assertThat(teams).contains(team1, team2);
    }

    @Test
    void getTeamById() {
        // when
        Team teamById = teamQueryService.getTeamById(team1.getId());

        // then
        assertThat(teamById).isEqualTo(team1);
    }

    @Test
    void isTeamLeader() {
        // when
        boolean isMember1TeamLeader = teamQueryService.isTeamLeader(team1.getId(), member1.getUsername());
        boolean isMember2TeamLeader = teamQueryService.isTeamLeader(team1.getId(), member2.getUsername());

        // then
        assertThat(isMember1TeamLeader).isEqualTo(true);
        assertThat(isMember2TeamLeader).isEqualTo(false);
    }
}