package com.example.waggle.domain.schedule.service.team;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.schedule.entity.*;
import com.example.waggle.domain.schedule.entity.Participation.ParticipationStatus;
import com.example.waggle.domain.schedule.repository.*;
import com.example.waggle.domain.schedule.service.schedule.ScheduleCommandService;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.exception.handler.TeamHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.security.SecurityUtil;
import com.example.waggle.web.dto.schedule.TeamRequest.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
    private final MemberQueryService memberQueryService;
    private final ScheduleCommandService scheduleCommandService;

    @Override
    public Long createTeam(Post request) {
        Member loginMember = memberQueryService.getSignInMember();

        Team createdTeam = Team.builder()
                .name(request.getName())
                .description(request.getDescription())
                .coverImageUrl(request.getCoverImageUrl())
                .teamColor(Enum.valueOf(TeamColor.class, request.getTeamColor()))
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
                .coverImageUrl(request.getCoverImageUrl())
                .teamColor(TeamColor.valueOf(request.getTeamColor()))
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
        validateCallerIsLeader(team);
        validateMaxTeamMemberSize(request, team);

        team.update(request);
        return team.getId();
    }

    @Override
    public void deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        validateCallerIsLeader(team);

        List<Schedule> allByTeamId = scheduleRepository.findAllByTeamId(teamId);
        allByTeamId.stream().forEach(schedule -> scheduleCommandService.deleteScheduleForHardReset(schedule.getId()));
        teamRepository.delete(team);
    }

    @Override
    public Long addTeamMember(Long teamId, Long memberId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        validateTeamMemberCount(team);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        validateMemberDuplication(team, member);
        addMemberToTeam(team, member);

        return team.getId();
    }

    @Override
    public void deleteTeamMemberByLeader(Long teamId, Long memberId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        validateCallerIsLeader(team);
        validateRemovedIsLeader(memberId, team);
        memberScheduleRepository.deleteAllByMemberId(memberId);
        teamMemberRepository.deleteAllByMemberIdAndTeamId(memberId, teamId);
    }

    @Override
    public void deleteTeamMemberByMyself(Long teamId, String username) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        validateRemovedIsLeader(member.getId(), team);

        memberScheduleRepository.deleteAllByMemberId(member.getId());
        teamMemberRepository.deleteAllByMemberIdAndTeamId(member.getId(), teamId);
    }

    @Override
    public void changeTeamLeader(Long teamId, Long memberId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

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
    public void respondToParticipation(Long teamId, Long memberId, boolean accept) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        validateCallerIsLeader(team);

        Participation participation = participationRepository.findByTeamIdAndUsername(teamId, member.getUsername())
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_PARTICIPATION_NOT_FOUND));

        if (accept) {
            validateTeamMemberCount(team);
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

    private static void validateMaxTeamMemberSize(Post request, Team team) {
        if (team.getTeamMembers().size() > request.getMaxTeamSize()) {
            throw new TeamHandler(ErrorStatus.TEAM_SIZE_IS_OVER_THAN_REQUEST_SIZE);
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
