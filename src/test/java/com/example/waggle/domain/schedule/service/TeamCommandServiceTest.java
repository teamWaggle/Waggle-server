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
<<<<<<< HEAD
import com.example.waggle.web.dto.member.MemberRequest.MemberCredentialsDto;
import com.example.waggle.web.dto.schedule.TeamRequest.TeamCreateDto;
=======
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.schedule.TeamRequest;
>>>>>>> develop
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

<<<<<<< HEAD
    private MemberCredentialsDto member1;

    private MemberCredentialsDto member2;

    private MemberCredentialsDto member3;
=======
    private MemberRequest.AccessDto member1;

    private MemberRequest.AccessDto member2;

    private MemberRequest.AccessDto member3;
>>>>>>> develop

    private TeamCreateDto team;

    private Long teamId;
    private Long member2Id;

    private Member member;


    @BeforeEach
    void setUp() {
        // Setup member
<<<<<<< HEAD
        member1 = MemberCredentialsDto.builder()
=======
        member1 = MemberRequest.AccessDto.builder()
>>>>>>> develop
                .password("12345678")
                .email("dasfk")
                .build();

<<<<<<< HEAD
        member2 = MemberCredentialsDto.builder()
=======
        member2 = MemberRequest.AccessDto.builder()
>>>>>>> develop
                .password("12345678")
                .email("aksdfhsafa")
                .build();

<<<<<<< HEAD
        member3 = MemberCredentialsDto.builder()
=======
        member3 = MemberRequest.AccessDto.builder()
>>>>>>> develop
                .password("12345678")
                .email("wldkfjk")
                .build();

<<<<<<< HEAD
        team = TeamCreateDto.builder()
=======
        team = TeamRequest.Post.builder()
>>>>>>> develop
                .teamColor("team_4")
                .maxTeamSize(4)
                .name("team")
                .description("team")
                .build();
        Long member1 = memberCommandService.signUp(this.member1);
        member2Id = memberCommandService.signUp(member2);
        memberCommandService.signUp(member3);
        member = memberQueryService.getMemberById(member1);
        teamId = teamCommandService.createTeam(team, member.getUsername());
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
    void createTeam() {
        // given
        TeamCreateDto createRequest = TeamCreateDto.builder()
                .name("test name")
                .description("test description")
                .maxTeamSize(10)
                .teamColor("team_1")
                .build();

        // when
        log.info("member username = {}", member.getUsername());
        Long createdTeamId = teamCommandService.createTeam(createRequest, member.getUsername());

        // then
        Team createdTeam = teamQueryService.getTeamById(createdTeamId);
        assertThat(createdTeam.getName()).isEqualTo(createRequest.getName());
        assertThat(createdTeam.getDescription()).isEqualTo(createRequest.getDescription());
        assertThat(createdTeam.getMaxTeamSize()).isEqualTo(createRequest.getMaxTeamSize());
    }


    @Test
    void updateTeam() {
        // given
        TeamCreateDto updateRequest = TeamCreateDto.builder()
                .name("updated name")
                .description("updated description")
                .teamColor("team_3")
                .maxTeamSize(15)
                .build();

        // when
        Long updatedTeamId = teamCommandService.updateTeam(teamId, member.getUsername(), updateRequest);

        // then
        Team updatedTeam = teamRepository.findById(updatedTeamId).get();
        assertThat(updatedTeam.getName()).isEqualTo(updateRequest.getName());
        assertThat(updatedTeam.getDescription()).isEqualTo(updateRequest.getDescription());
        assertThat(updatedTeam.getMaxTeamSize()).isEqualTo(updateRequest.getMaxTeamSize());
    }

    @Test
    void deleteTeam() {
        // when
        teamCommandService.deleteTeam(teamId, member.getUsername());

        // then
        assertThat(teamRepository.existsById(teamId)).isEqualTo(false);
    }


    @Test
    void addTeamMember() {

        // when
        Long updatedTeamId = teamCommandService.addTeamMember(teamId, member2Id);

        // then
        List<TeamMember> teamById = teamMemberRepository.findTeamMemberByTeamId(updatedTeamId);
        assertThat(teamById.size()).isEqualTo(2);
    }


    @Test
    @Transactional
    void deleteTeamMember() {
        // given
        Long teamMember = teamCommandService.addTeamMember(teamId, member2Id);
        // when
        Team teamById = teamQueryService.getTeamById(teamId);
        Member memberById = memberQueryService.getMemberById(member2Id);
        teamCommandService.deleteTeamMemberByMyself(teamId, memberById.getUsername());

        // then
        List<TeamMember> teamMemberByTeamId = teamMemberRepository.findTeamMemberByTeamId(teamId);
        assertThat(teamMemberByTeamId.size()).isEqualTo(1);
    }


    @Test
    @Transactional
    void changeTeamLeader() {
        // given
        Long updatedTeamId = teamCommandService.addTeamMember(teamId, member2Id);

        // when
        teamCommandService.changeTeamLeader(teamId, member2Id, member.getUsername());

        // then
        Team teamById = teamQueryService.getTeamById(teamId);
        Member memberById = memberQueryService.getMemberById(member2Id);
        assertThat(teamById.getLeader().getUsername()).isEqualTo(memberById.getUsername());
    }

    @Test
    void requestParticipation() {
        // when
        Member memberById = memberQueryService.getMemberById(member2Id);
        teamCommandService.requestParticipation(teamId, memberById.getUsername());

        // then
        Participation participation = participationRepository.findByTeamIdAndUsername(teamId,
                memberById.getUsername()).get();
        assertThat(participation.getTeamId()).isEqualTo(teamId);
        assertThat(participation.getUsername()).isEqualTo(memberById.getUsername());
        assertThat(participation.getStatus()).isEqualTo(ParticipationStatus.PENDING);
    }

    @Test
    void respondToParticipation() {
        // given
        Member memberById = memberQueryService.getMemberById(member2Id);
        teamCommandService.requestParticipation(teamId, memberById.getUsername());

        // when
        teamCommandService.respondToParticipation(teamId, member2Id, member.getUsername(), true);

        // then
        Participation participationOfMember2 = participationRepository.findByTeamIdAndUsername(teamId,
                memberById.getUsername()).get();

        assertThat(participationOfMember2.getTeamId()).isEqualTo(teamId);
        assertThat(participationOfMember2.getUsername()).isEqualTo(memberById.getUsername());
        assertThat(participationOfMember2.getStatus()).isEqualTo(ParticipationStatus.ACCEPTED);
    }
}