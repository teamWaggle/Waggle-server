package com.example.waggle.domain.schedule.service.team;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.schedule.entity.Participation;
import com.example.waggle.domain.schedule.entity.ParticipationStatus;
import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.entity.Team;
import com.example.waggle.domain.schedule.entity.TeamColor;
import com.example.waggle.domain.schedule.entity.TeamMember;
import com.example.waggle.domain.schedule.repository.MemberScheduleRepository;
import com.example.waggle.domain.schedule.repository.ParticipationRepository;
import com.example.waggle.domain.schedule.repository.ScheduleRepository;
import com.example.waggle.domain.schedule.repository.TeamMemberRepository;
import com.example.waggle.domain.schedule.repository.TeamRepository;
import com.example.waggle.domain.schedule.service.schedule.ScheduleCommandService;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.exception.handler.TeamHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.schedule.TeamRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TeamCommandServiceImpl implements TeamCommandService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberScheduleRepository memberScheduleRepository;
    private final ParticipationRepository participationRepository;
    private final ScheduleCommandService scheduleCommandService;
    private final int teamCapacityLimit = 50;

    @Override
    public Long createTeam(TeamRequest createTeamRequest, Member member) {
        Team team = buildTeam(createTeamRequest, member);
        teamRepository.save(team);
        addMemberToTeam(team, member);
        return team.getId();
    }

    private Team buildTeam(TeamRequest createTeamRequest, Member member) {
        return Team.builder()
                .name(createTeamRequest.getName())
                .description(createTeamRequest.getDescription())
                .coverImageUrl(createTeamRequest.getCoverImageUrl())
                .teamColor(TeamColor.valueOf(createTeamRequest.getTeamColor()))
                .maxTeamSize(createTeamRequest.getMaxTeamSize())
                .leader(member)
                .build();
    }

    @Override
    public Long updateTeam(Long teamId, TeamRequest updateTeamRequest, Member member) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        validateCallerIsLeader(team, member);
        validateTeamMemberIsOverRequestSize(updateTeamRequest, team);
        team.update(updateTeamRequest);
        return team.getId();
    }


    @Override
    public void deleteTeam(Long teamId, Member member) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        validateCallerIsLeader(team, member);
        List<Schedule> allByTeamId = scheduleRepository.findAllByTeamId(teamId);
        allByTeamId.stream()
                .forEach(schedule -> scheduleCommandService.deleteScheduleForHardReset(schedule.getId()));
        teamRepository.delete(team);
    }

    @Override
    public Long addTeamMember(Long teamId, Long newMemberId, Member member) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        Member newMember = memberRepository.findById(newMemberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        validateCallerIsLeader(team, member);
        validateTeamMemberCount(team);
        validateMemberDuplication(team, newMember);
        validateLimitOfTeamCapacity(team);
        addMemberToTeam(team, member);

        return team.getId();
    }

    @Override
    public void deleteTeamMemberByLeader(Long teamId, Long memberId, Member leader) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        validateCallerIsLeader(team, leader);
        validateRemovedIsLeader(memberId, team);
        memberScheduleRepository.deleteAllByMemberId(memberId);
        teamMemberRepository.deleteAllByMemberIdAndTeamId(memberId, teamId);
    }

    @Override
    public void deleteTeamMemberByMyself(Long teamId, Member member) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        validateRemovedIsLeader(member.getId(), team);

        memberScheduleRepository.deleteAllByMemberId(member.getId());
        teamMemberRepository.deleteAllByMemberIdAndTeamId(member.getId(), teamId);
    }

    @Override
    public void changeTeamLeader(Long teamId, Long memberId, Member leader) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        validateCallerIsLeader(team, leader);
        validateMemberBelongsToTeam(team, member);
        team.updateLeader(member);
    }

    @Override
    public void requestParticipation(Long teamId, Member member) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        validateNonExistenceOfParticipationRequest(team, member);

        Participation participation = buildParticipation(member, team);
        participationRepository.save(participation);
    }

    @Override
    public void respondToParticipation(Long teamId, Long memberId, Member leader,
                                       boolean accept) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        validateCallerIsLeader(team, leader);

        Participation participation = participationRepository.findByTeamAndMember(team,
                        member)
                .orElseThrow(() -> new TeamHandler(
                        ErrorStatus.TEAM_PARTICIPATION_NOT_FOUND));

        if (accept) {
            validateTeamMemberCount(team);
            validateLimitOfTeamCapacity(team);
            participation.setStatus(ParticipationStatus.ACCEPTED);
            addMemberToTeam(team, member);
        } else {
            participation.setStatus(ParticipationStatus.REJECTED);
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


    private void validateCallerIsLeader(Team team, Member member) {
        if (!team.getLeader().equals(member)) {
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

    private void validateNonExistenceOfParticipationRequest(Team team, Member
            member) {
        if (participationRepository.existsByTeamAndMember(team, member)) {
            throw new TeamHandler(
                    ErrorStatus.TEAM_PARTICIPATION_REQUEST_ALREADY_EXISTS);
        }
    }

    private static void validateTeamMemberCount(Team team) {
        if (team.getTeamMembers().size() == team.getMaxTeamSize()) {
            throw new TeamHandler(ErrorStatus.TEAM_MEMBER_CANNOT_BE_EXCEEDED);
        }
    }

    private static void validateRemovedIsLeader(Long memberId, Team team) {
        if (team.getLeader().getId().equals(memberId)) {
            throw new TeamHandler(ErrorStatus.TEAM_LEADER_CANNOT_BE_REMOVED);
        }
    }

    private static void validateTeamMemberIsOverRequestSize(TeamRequest request, Team team) {
        if (team.getTeamMembers().size() > request.getMaxTeamSize()) {
            throw new TeamHandler(
                    ErrorStatus.TEAM_SIZE_IS_OVER_THAN_REQUEST_SIZE);
        }
    }

    private void validateLimitOfTeamCapacity(Team team) {
        if (team.getTeamMembers().size() == teamCapacityLimit) {
            throw new TeamHandler(
                    ErrorStatus.TEAM_SIZE_IS_OVER_THAN_MAX_SIZE);
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

    private static Participation buildParticipation(Member member, Team team) {
        return Participation.builder()
                .team(team)
                .member(member)
                .status(ParticipationStatus.PENDING)
                .build();
    }
}
