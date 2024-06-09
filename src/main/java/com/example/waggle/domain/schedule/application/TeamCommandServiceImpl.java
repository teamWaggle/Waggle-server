package com.example.waggle.domain.schedule.application;

import com.example.waggle.domain.board.persistence.dao.board.jpa.BoardRepository;
import com.example.waggle.domain.member.persistence.dao.jpa.MemberRepository;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.member.persistence.entity.Role;
import com.example.waggle.domain.notification.persistence.dao.NotificationRepository;
import com.example.waggle.domain.notification.persistence.entity.Notification;
import com.example.waggle.domain.notification.persistence.entity.NotificationType;
import com.example.waggle.domain.notification.presentation.dto.NotificationRequest.ParticipationDto;
import com.example.waggle.domain.schedule.persistence.dao.jpa.MemberScheduleRepository;
import com.example.waggle.domain.schedule.persistence.dao.jpa.ParticipationRepository;
import com.example.waggle.domain.schedule.persistence.dao.jpa.TeamMemberRepository;
import com.example.waggle.domain.schedule.persistence.dao.jpa.TeamRepository;
import com.example.waggle.domain.schedule.persistence.entity.*;
import com.example.waggle.domain.schedule.presentation.dto.team.TeamRequest;
import com.example.waggle.exception.object.handler.MemberHandler;
import com.example.waggle.exception.object.handler.TeamHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.global.service.aws.AwsS3Service;
import com.example.waggle.global.util.MediaUtil;
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
    private final MemberScheduleRepository memberScheduleRepository;
    private final BoardRepository boardRepository;
    private final ParticipationRepository participationRepository;
    private final NotificationRepository notificationRepository;
    private final AwsS3Service awsS3Service;
    private final int teamCapacityLimit = 20;

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
                .isPrivate(createTeamRequest.getIsPrivate())
                .leader(member)
                .build();
    }

    @Override
    public Long updateTeam(Long teamId, TeamRequest updateTeamRequest, Member member) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        validateCallerIsLeader(team, member);
        if (MediaUtil.validateRemoveImgInS3(team.getCoverImageUrl(), updateTeamRequest.getCoverImageUrl())) {
            awsS3Service.deleteFile(team.getCoverImageUrl());
        }
        team.update(updateTeamRequest);
        return team.getId();
    }


    @Override
    public void deleteTeam(Long teamId, Member member) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        validateCallerIsLeader(team, member);
        awsS3Service.deleteFile(team.getCoverImageUrl());
        teamRepository.deleteTeamWithRelations(teamId);
    }


    @Override
    public void deleteTeamMemberByLeader(Long teamId, Long memberId, Member leader) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        validateCallerIsLeader(team, leader);
        validateTargetIsNotLeader(memberId, team);

        participationRepository.deleteByMemberIdAndTeamId(memberId, teamId);
        memberScheduleRepository.deleteAllByMemberId(memberId);
        boardRepository.deleteBoardsWithRelationsByMemberId(memberId);
        teamMemberRepository.deleteAllByMemberIdAndTeamId(memberId, teamId);
    }

    @Override
    public void deleteTeamMemberByMyself(Long teamId, Member member) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        validateTargetIsNotLeader(member.getId(), team);

        memberScheduleRepository.deleteAllByMemberId(member.getId());
        boardRepository.deleteBoardsWithRelationsByMemberId(member.getId());
        participationRepository.deleteByMemberAndTeam(member, team);
        teamMemberRepository.deleteAllByMemberIdAndTeamId(member.getId(), teamId);
    }

    @Override
    public void deleteTeamByAdmin(Long teamId, Member member) {
        if (!member.getRole().equals(Role.ADMIN)) {
            throw new MemberHandler(ErrorStatus.MEMBER_ACCESS_DENIED_BY_AUTHORIZATION);
        }
        teamRepository.deleteTeamWithRelations(teamId);
    }

    @Override
    public void changeTeamLeader(Long teamId, Long memberId, Member leader) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Participation leaderParticipation = buildParticipation(leader, team, ParticipationStatus.ACCEPTED);


        validateCallerIsLeader(team, leader);
        validateTargetIsNotLeader(memberId, team);
        validateMemberBelongsToTeam(team, member);
        team.updateLeader(member);
        participationRepository.save(leaderParticipation);
        participationRepository.deleteByMemberAndTeam(member, team);
    }

    @Override
    public Long requestParticipation(Long teamId, Member member) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        validateNonExistenceOfParticipationRequest(team, member);

        Participation participation = buildParticipation(member, team, ParticipationStatus.PENDING);
        participationRepository.save(participation);

        ParticipationDto content = buildParticipationDto(team);
        notificationRepository.save(
                Notification.of(member,
                        team.getLeader(),
                        NotificationType.PARTICIPATION_REQUEST,
                        content)
        );

        return participation.getId();
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
            validateLimitOfTeamCapacity(team);
            participation.setStatus(ParticipationStatus.ACCEPTED);
            addMemberToTeam(team, member);
            ParticipationDto content = buildParticipationDto(team);
            notificationRepository.save(
                    Notification.of(member,
                            participation.getMember(),
                            NotificationType.PARTICIPATION_APPROVE,
                            content)
            );
        } else {
            participationRepository.deleteByMemberAndTeam(member, team);
        }
        participationRepository.save(participation);
    }

    private static ParticipationDto buildParticipationDto(Team team) {
        return ParticipationDto.builder()
                .teamId(team.getId())
                .teamName(team.getName())
                .build();
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

    private void validateNonExistenceOfParticipationRequest(Team team, Member member) {
        if (participationRepository.existsByTeamAndMember(team, member)) {
            throw new TeamHandler(ErrorStatus.TEAM_PARTICIPATION_REQUEST_ALREADY_EXISTS);
        }
    }

    private static void validateTargetIsNotLeader(Long memberId, Team team) {
        if (team.getLeader().getId().equals(memberId)) {
            throw new TeamHandler(ErrorStatus.TEAM_LEADER_CANNOT_BE_REMOVED);
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

    private static Participation buildParticipation(Member member, Team team, ParticipationStatus status) {
        return Participation.builder()
                .team(team)
                .member(member)
                .status(status)
                .build();
    }
}
