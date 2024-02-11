package com.example.waggle.domain.schedule.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.schedule.entity.Participation;
import com.example.waggle.domain.schedule.entity.Participation.ParticipationStatus;
import com.example.waggle.domain.schedule.entity.Team;
import com.example.waggle.domain.schedule.entity.TeamMember;
import com.example.waggle.domain.schedule.repository.ParticipationRepository;
import com.example.waggle.domain.schedule.repository.TeamMemberRepository;
import com.example.waggle.domain.schedule.repository.TeamRepository;
import com.example.waggle.domain.schedule.service.team.TeamCommandService;
import com.example.waggle.domain.schedule.service.team.TeamQueryService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.schedule.TeamRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@WithMockCustomUser
@SpringBootTest
class TeamCommandServiceTest {

    @Autowired
    TeamCommandService teamCommandService;
    @Autowired
    TeamQueryService teamQueryService;
    @Autowired
    MemberCommandService memberCommandService;
    @Autowired
    MemberQueryService memberQueryService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    TeamMemberRepository teamMemberRepository;
    @Autowired
    ParticipationRepository participationRepository;
    @Autowired
    DatabaseCleanUp databaseCleanUp;

    private MemberRequest.RegisterDto member1;

    private MemberRequest.RegisterDto member2;

    private MemberRequest.RegisterDto member3;

    private TeamRequest.Post team;

    private Long teamId;


    @BeforeEach
    void setUp() {
        // Setup member
        member1 = MemberRequest.RegisterDto.builder()
                .username("member1")
                .password("12345678")
                .email("dasfk")
                .nickname("lksadfjklj")
                .build();

        member2 = MemberRequest.RegisterDto.builder()
                .username("member2")
                .password("12345678")
                .email("aksdfhsafa")
                .nickname("sadlfkdsfjkw")
                .build();

        member3 = MemberRequest.RegisterDto.builder()
                .username("member3")
                .password("12345678")
                .email("wldkfjk")
                .nickname("jdhskjfhac")
                .build();

        team = TeamRequest.Post.builder().colorScheme("hi").maxTeamSize(4).name("team").description("team").build();
        memberCommandService.signUp(member1);
        memberCommandService.signUp(member2);
        memberCommandService.signUp(member3);
        teamId = teamCommandService.createTeam(team, "member1");
    }

    private void addMemberToTeam(Team team, Member member) {
        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .member(member)
                .build();
        teamMember.addTeamMember(team, member);
        teamMemberRepository.save(teamMember);
    }

    @AfterEach
    void clean() {
        databaseCleanUp.truncateAllEntity();
    }


    @Test
    @Transactional
    void createTeam() {
        // given
        TeamRequest.Post createRequest = TeamRequest.Post.builder()
                .name("test name")
                .description("test description")
                .maxTeamSize(10)
                .colorScheme("red")
                .build();

        // when
        Long createdTeamId = teamCommandService.createTeam(createRequest);

        // then
        Team createdTeam = teamQueryService.getTeamById(createdTeamId);
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
                .colorScheme("zz")
                .maxTeamSize(15)
                .build();

        // when
        Long updatedTeamId = teamCommandService.updateTeam(teamId, updateRequest);

        // then
        Team updatedTeam = teamRepository.findById(updatedTeamId).get();
        assertThat(updatedTeam.getName()).isEqualTo(updateRequest.getName());
        assertThat(updatedTeam.getDescription()).isEqualTo(updateRequest.getDescription());
        assertThat(updatedTeam.getMaxTeamSize()).isEqualTo(updateRequest.getMaxTeamSize());
    }

    @Test
    void deleteTeam() {
        // when
        teamCommandService.deleteTeam(teamId);

        // then
        assertThat(teamRepository.existsById(teamId)).isEqualTo(false);
    }


    @Test
    @Transactional
    void addTeamMember() {

        // when
        Long updatedTeamId = teamCommandService.addTeamMember(teamId, member2.getUsername());

        // then
        Team teamById = teamQueryService.getTeamById(updatedTeamId);
        assertThat(teamById.getTeamMembers().size()).isEqualTo(2);
    }


    @Test
    @Transactional
    void deleteTeamMember() {
        // given
        Long teamMember = teamCommandService.addTeamMember(teamId, "member2");
        // when
        Team teamById = teamQueryService.getTeamById(teamId);
        teamCommandService.deleteTeamMember(teamId, "member2");

        // then
        List<TeamMember> teamMemberByTeamId = teamMemberRepository.findTeamMemberByTeamId(teamId);
        assertThat(teamMemberByTeamId.size()).isEqualTo(1);
        assertThat(teamMemberByTeamId.get(0).getMember().getUsername()).isEqualTo("member1");
    }


    @Test
    @Transactional
    void changeTeamLeader() {
        // given
        Long updatedTeamId = teamCommandService.addTeamMember(teamId, member2);

        // when
        teamCommandService.changeTeamLeader(teamId, member2.getUsername());

        // then
        Team teamById = teamQueryService.getTeamById(teamId);
        assertThat(teamById.getLeader().getUsername()).isEqualTo(member2.getUsername());
    }

    @Test
    void requestParticipation() {
        // when
        teamCommandService.requestParticipation(teamId, member2.getUsername());

        // then
        Participation participation = participationRepository.findByTeamIdAndUsername(teamId,
                member2.getUsername()).get();
        assertThat(participation.getTeamId()).isEqualTo(teamId);
        assertThat(participation.getUsername()).isEqualTo(member2.getUsername());
        assertThat(participation.getStatus()).isEqualTo(ParticipationStatus.PENDING);
    }

    @Test
    void respondToParticipation() {
        // given
        teamCommandService.requestParticipation(teamId, member2.getUsername());
        teamCommandService.requestParticipation(teamId, member3.getUsername());

        // when
        teamCommandService.respondToParticipation(teamId, member2.getUsername(), true);
        teamCommandService.respondToParticipation(teamId, member3.getUsername(), false);

        // then
        Participation participationOfMember2 = participationRepository.findByTeamIdAndUsername(teamId,
                member2.getUsername()).get();
        assertThat(participationOfMember2.getTeamId()).isEqualTo(teamId);
        assertThat(participationOfMember2.getUsername()).isEqualTo(member2.getUsername());
        assertThat(participationOfMember2.getStatus()).isEqualTo(ParticipationStatus.ACCEPTED);

        Participation participationOfMember3 = participationRepository.findByTeamIdAndUsername(teamId,
                member3.getUsername()).get();
        assertThat(participationOfMember3.getTeamId()).isEqualTo(teamId);
        assertThat(participationOfMember3.getUsername()).isEqualTo(member3.getUsername());
        assertThat(participationOfMember3.getStatus()).isEqualTo(ParticipationStatus.REJECTED);
    }
}