package com.example.waggle.domain.schedule.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.schedule.domain.Participation;
import com.example.waggle.domain.schedule.domain.Participation.ParticipationStatus;
import com.example.waggle.domain.schedule.domain.Team;
import com.example.waggle.domain.schedule.domain.TeamMember;
import com.example.waggle.domain.schedule.repository.ParticipationRepository;
import com.example.waggle.domain.schedule.repository.TeamMemberRepository;
import com.example.waggle.domain.schedule.repository.TeamRepository;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.web.dto.schedule.TeamRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@WithMockCustomUser
@Transactional
@SpringBootTest
class TeamCommandServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;
    @Autowired
    TeamCommandService teamCommandService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    TeamMemberRepository teamMemberRepository;
    @Autowired
    ParticipationRepository participationRepository;

    private Member member1;
    private Member member2;
    private Member member3;
    private Team team;


    @BeforeEach
    void setUp() {
        // Setup member
        member1 = Member.builder()
                .username("member1")
                .password("12345678")
                .build();
        memberRepository.save(member1);

        member2 = Member.builder()
                .username("member2")
                .password("12345678")
                .build();
        memberRepository.save(member2);

        member3 = Member.builder()
                .username("member3")
                .password("12345678")
                .build();
        memberRepository.save(member3);

        // Setup team
        team = Team.builder()
                .name("team1")
                .description("team1 description")
                .leader(member1)
                .maxTeamSize(4)
                .build();
        teamRepository.save(team);

        // Setup teamMember
        TeamMember teamMember = TeamMember.builder()
                .member(member1)
                .team(team)
                .build();
        teamMember.addTeamMember(team, member1);
    }

    @AfterEach
    void clean() {
        databaseCleanUp.truncateAllEntity();
    }


    @Test
    void createTeam() {
        // given
        TeamRequest.Post createRequest = TeamRequest.Post.builder()
                .name("test name")
                .description("test description")
                .maxTeamSize(10)
                .build();

        // when
        Long createdTeamId = teamCommandService.createTeam(createRequest);

        // then
        Team createdTeam = teamRepository.findById(createdTeamId).get();
        assertThat(createdTeam.getName()).isEqualTo(createRequest.getName());
        assertThat(createdTeam.getDescription()).isEqualTo(createRequest.getDescription());
        assertThat(createdTeam.getMaxTeamSize()).isEqualTo(createRequest.getMaxTeamSize());
        assertThat(createdTeam.getTeamMembers().size()).isEqualTo(1);
        assertThat(createdTeam.getTeamMembers().get(0).getMember().getUsername()).isEqualTo(member1.getUsername());
    }


    @Test
    void updateTeam() {
        // given
        TeamRequest.Post updateRequest = TeamRequest.Post.builder()
                .name("updated name")
                .description("updated description")
                .maxTeamSize(15)
                .build();

        // when
        Long updatedTeamId = teamCommandService.updateTeam(team.getId(), updateRequest);

        // then
        Team updatedTeam = teamRepository.findById(updatedTeamId).get();
        assertThat(updatedTeam.getName()).isEqualTo(updateRequest.getName());
        assertThat(updatedTeam.getDescription()).isEqualTo(updateRequest.getDescription());
        assertThat(updatedTeam.getMaxTeamSize()).isEqualTo(updateRequest.getMaxTeamSize());
    }

    @Test
    void deleteTeam() {
        // when
        teamCommandService.deleteTeam(team.getId());

        // then
        assertThat(teamRepository.existsById(team.getId())).isEqualTo(false);
    }


    @Test
    void addTeamMember() {

        // when
        Long updatedTeamId = teamCommandService.addTeamMember(team.getId(), member2.getUsername());

        // then
        Team updatedTeam = teamRepository.findById(updatedTeamId).get();
        assertThat(updatedTeam.getTeamMembers().size()).isEqualTo(2);
    }


    @Test
    void deleteTeamMember() {
        // given
        Long updatedTeamId = teamCommandService.addTeamMember(team.getId(), member2.getUsername());

        // when
        teamCommandService.deleteTeamMember(updatedTeamId, member2.getUsername());

        // then
        Team updatedTeam = teamRepository.findById(updatedTeamId).get();
        assertThat(updatedTeam.getTeamMembers().size()).isEqualTo(1);
        assertThat(updatedTeam.getTeamMembers().get(0).getMember().getUsername()).isEqualTo(member1.getUsername());
    }


    @WithMockCustomUser
    @Test
    void changeTeamLeader() {
        // given
        Long updatedTeamId = teamCommandService.addTeamMember(team.getId(), member2.getUsername());

        // when
        teamCommandService.changeTeamLeader(team.getId(), member2.getUsername());

        // then
        Team updatedTeam = teamRepository.findById(updatedTeamId).get();
        assertThat(updatedTeam.getLeader().getUsername()).isEqualTo(member2.getUsername());
    }

    @Test
    void requestParticipation() {
        // when
        teamCommandService.requestParticipation(team.getId(), member2.getUsername());

        // then
        Participation participation = participationRepository.findByTeamIdAndUsername(team.getId(),
                member2.getUsername()).get();
        assertThat(participation.getTeamId()).isEqualTo(team.getId());
        assertThat(participation.getUsername()).isEqualTo(member2.getUsername());
        assertThat(participation.getStatus()).isEqualTo(ParticipationStatus.PENDING);
    }

    @Test
    void respondToParticipation() {
        // given
        teamCommandService.requestParticipation(team.getId(), member2.getUsername());
        teamCommandService.requestParticipation(team.getId(), member3.getUsername());

        // when
        teamCommandService.respondToParticipation(team.getId(), member2.getUsername(), true);
        teamCommandService.respondToParticipation(team.getId(), member3.getUsername(), false);

        // then
        Participation participationOfMember2 = participationRepository.findByTeamIdAndUsername(team.getId(),
                member2.getUsername()).get();
        assertThat(participationOfMember2.getTeamId()).isEqualTo(team.getId());
        assertThat(participationOfMember2.getUsername()).isEqualTo(member2.getUsername());
        assertThat(participationOfMember2.getStatus()).isEqualTo(ParticipationStatus.ACCEPTED);

        Participation participationOfMember3 = participationRepository.findByTeamIdAndUsername(team.getId(),
                member3.getUsername()).get();
        assertThat(participationOfMember3.getTeamId()).isEqualTo(team.getId());
        assertThat(participationOfMember3.getUsername()).isEqualTo(member3.getUsername());
        assertThat(participationOfMember3.getStatus()).isEqualTo(ParticipationStatus.REJECTED);
    }
}