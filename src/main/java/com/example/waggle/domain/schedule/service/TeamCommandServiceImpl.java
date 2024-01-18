package com.example.waggle.domain.schedule.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.schedule.entity.Participation;
import com.example.waggle.domain.schedule.entity.Participation.ParticipationStatus;
import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.entity.Team;
import com.example.waggle.domain.schedule.entity.TeamMember;
import com.example.waggle.domain.schedule.repository.ParticipationRepository;
import com.example.waggle.domain.schedule.repository.ScheduleRepository;
import com.example.waggle.domain.schedule.repository.TeamMemberRepository;
import com.example.waggle.domain.schedule.repository.TeamRepository;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.exception.handler.TeamHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.security.SecurityUtil;
import com.example.waggle.web.dto.schedule.TeamRequest.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Transactional
@Service
public class TeamCommandServiceImpl implements TeamCommandService {

    //REPOSITORY
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ScheduleRepository scheduleRepository;
    private final ParticipationRepository participationRepository;
    //QUERY_SERVICE
    private final MemberQueryService memberQueryService;
    //COMMAND_SERVICE
    private final ScheduleCommandService scheduleCommandService;

    @Override
    public Long createTeam(Post request) {
        Member loginMember = memberQueryService.getSignInMember();

        Team createdTeam = Team.builder()
                .name(request.getName())
                .description(request.getDescription())
//                .coverImageUrl(request.get)
                .colorScheme(request.getColorScheme())
                .maxTeamSize(request.getMaxTeamSize())
                .leader(loginMember)
                .build();

        Team team = teamRepository.save(createdTeam);
        addMemberToTeam(team, loginMember);

        return team.getId();
    }

    @Override
    public Long createTeam(Post request, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Team createdTeam = Team.builder()
                .name(request.getName())
                .description(request.getDescription())
//                .coverImageUrl(request.get)
                .colorScheme(request.getColorScheme())
                .maxTeamSize(request.getMaxTeamSize())
                .leader(member)
                .build();

        Team team = teamRepository.save(createdTeam);
        addMemberToTeam(team, member);

        return team.getId();
    }

    @Override
    public Long updateTeam(Long teamId, Post request) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        team.update(request);
        return team.getId();
    }

    @Override
    public void deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        List<Schedule> allByTeamId = scheduleRepository.findAllByTeamId(teamId);
        allByTeamId.stream().forEach(schedule -> scheduleCommandService.deleteSchedule(schedule.getId()));
        teamRepository.delete(team);
    }

    @Override
    public Long addTeamMember(Long teamId, String username) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        Member member = memberQueryService.getMemberByUsername(username);

        validateMemberDuplication(team, member);

        addMemberToTeam(team, member);

        return team.getId();
    }

    @Override
    public void deleteTeamMember(Long teamId, String username) {
        TeamMember teamMember = teamMemberRepository.findTeamMemberByMemberUsernameAndTeamId(username, teamId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        teamMember.removeTeamMember();
    }

    @Override
    public void changeTeamLeader(Long teamId, String username) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        Member member = memberQueryService.getMemberByUsername(username);

        validateCallerIsLeader(team);
        validateMemberBelongsToTeam(team, member);

        team.updateLeader(member);
    }

    @Override
    public void requestParticipation(Long teamId, String username) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        Member member = memberQueryService.getMemberByUsername(username);

        validateNonExistenceOfParticipationRequest(team, member);

        Participation participation = Participation.builder()
                .teamId(teamId)
                .username(username)
                .status(ParticipationStatus.PENDING)
                .build();

        participationRepository.save(participation);
    }

    @Override
    public void respondToParticipation(Long teamId, String username, boolean accept) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        Member member = memberQueryService.getMemberByUsername(username);

        validateCallerIsLeader(team);

        Participation participation = participationRepository.findByTeamIdAndUsername(teamId, username)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_PARTICIPATION_NOT_FOUND));

        if (accept) {
            participation.setStatus(Participation.ParticipationStatus.ACCEPTED);
            addMemberToTeam(team, member);
        } else {
            participation.setStatus(Participation.ParticipationStatus.REJECTED);
        }
        participationRepository.save(participation);
    }


    private void validateMemberDuplication(Team team, Member member) {
        boolean isValid = team.getTeamMembers().stream()
                .noneMatch(teamMember -> teamMember.getMember().equals(member));
        if (!isValid) {
            throw new TeamHandler(ErrorStatus.TEAM_MEMBER_ALREADY_EXISTS);
        }
    }

    private void validateCallerIsLeader(Team team) {
        if (!team.getLeader().getUsername().equals(SecurityUtil.getCurrentUsername())) {
            throw new TeamHandler(ErrorStatus.TEAM_LEADER_UNAUTHORIZED);
        }
    }

    private void validateMemberBelongsToTeam(Team team, Member member) {
        boolean isMember = team.getTeamMembers().stream()
                .anyMatch(tm -> tm.getMember().equals(member));
        if (!isMember) {
            throw new TeamHandler(ErrorStatus.TEAM_MEMBER_NOT_IN_TEAM);
        }
    }

    private void validateNonExistenceOfParticipationRequest(Team team, Member member) {
        if (participationRepository.existsByTeamIdAndUsername(team.getId(), member.getUsername())) {
            throw new TeamHandler(ErrorStatus.TEAM_PARTICIPATION_REQUEST_ALREADY_EXISTS);
        }
    }

    private void addMemberToTeam(Team team, Member member) {
        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .member(member)
                .build();
        teamMember.addTeamMember(team, member);
        teamMemberRepository.save(teamMember);
    }

}
