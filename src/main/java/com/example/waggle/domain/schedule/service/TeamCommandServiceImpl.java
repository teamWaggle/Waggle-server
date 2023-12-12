package com.example.waggle.domain.schedule.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.schedule.domain.Team;
import com.example.waggle.domain.schedule.domain.TeamMember;
import com.example.waggle.domain.schedule.repository.TeamMemberRepository;
import com.example.waggle.domain.schedule.repository.TeamRepository;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.exception.handler.TeamHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.domain.board.service.BoardService;
import com.example.waggle.global.security.SecurityUtil;
import com.example.waggle.web.dto.schedule.TeamRequest.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class TeamCommandServiceImpl implements TeamCommandService {

    private final MemberQueryService memberQueryService;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final BoardService utilService;

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

        TeamMember teamMember = TeamMember.builder().build();
        teamMember.addTeamMember(team, loginMember);

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
        teamRepository.delete(team);
    }

    @Override
    public Long addTeamMember(Long teamId, String username) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        Member member = memberQueryService.getMemberByUsername(username);

        if (!validateMemberDuplication(team, member)) {
            throw new TeamHandler(ErrorStatus.TEAM_MEMBER_ALREADY_EXISTS);
        }

        TeamMember teamMember = TeamMember.builder().build();
        teamMember.addTeamMember(team, member);
        teamMemberRepository.save(teamMember);

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

        checkIfCallerIsLeader(team);
        checkIfMemberBelongsToTeam(team, member);

        team.updateLeader(member);
    }

    private boolean validateMemberDuplication(Team team, Member member) {
        return team.getTeamMembers().stream()
                .noneMatch(teamMember -> teamMember.getMember().equals(member));
    }

    private void checkIfCallerIsLeader(Team team) {
        if (!team.getLeader().getUsername().equals(SecurityUtil.getCurrentUsername())) {
            throw new TeamHandler(ErrorStatus.TEAM_LEADER_UNAUTHORIZED);
        }
    }


    private void checkIfMemberBelongsToTeam(Team team, Member member) {
        boolean isMember = team.getTeamMembers().stream()
                .anyMatch(tm -> tm.getMember().equals(member));
        if (!isMember) {
            throw new TeamHandler(ErrorStatus.TEAM_MEMBER_NOT_IN_TEAM);
        }
    }

}
