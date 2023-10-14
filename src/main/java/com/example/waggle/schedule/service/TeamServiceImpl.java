package com.example.waggle.schedule.service;

import com.example.waggle.commons.exception.CustomAlertException;
import com.example.waggle.member.domain.Member;
import com.example.waggle.member.domain.TeamMember;
import com.example.waggle.member.dto.MemberSummaryDto;
import com.example.waggle.member.repository.MemberRepository;
import com.example.waggle.member.repository.TeamMemberRepository;
import com.example.waggle.schedule.domain.Team;
import com.example.waggle.schedule.dto.TeamDto;
import com.example.waggle.schedule.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.waggle.commons.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.example.waggle.commons.exception.ErrorCode.TEAM_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TeamServiceImpl implements TeamService {
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Override
    public List<TeamDto> getTeams() {
        List<Team> result = teamRepository.findAll();
        return result.stream().map(TeamDto::toDto).collect(Collectors.toList());
    }

    @Override
    public List<TeamDto> getTeamsByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomAlertException(MEMBER_NOT_FOUND));

        List<Team> teams = new ArrayList<>();
        List<TeamMember> teamMembers = member.getTeamMembers();
        for (TeamMember teamMember : teamMembers) {
            teams.add(teamMember.getTeam());
        }

        return teams.stream().map(TeamDto::toDto).collect(Collectors.toList());
    }

    @Override
    public TeamDto getTeamById(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomAlertException(TEAM_NOT_FOUND));
        return TeamDto.toDto(team);
    }

    @Override
    public List<MemberSummaryDto> getTeamMembers(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomAlertException(TEAM_NOT_FOUND));

        return team.getTeamMembers().stream()
                .map(TeamMember::getMember)
                .map(MemberSummaryDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Long createTeam(TeamDto teamDto, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomAlertException(MEMBER_NOT_FOUND));

        Team team = teamRepository.save(teamDto.toEntity(member));
        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .member(member).build();
        teamMember.addTeamMember(team, member);
        teamMemberRepository.save(teamMember);
        return team.getId();
    }

    @Override
    @Transactional
    public Long addMember(Long teamId, String username) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomAlertException(TEAM_NOT_FOUND));
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomAlertException(MEMBER_NOT_FOUND));

        if (validateMemberDuplication(team, member)) {
            TeamMember teamMember = TeamMember.builder()
                    .team(team)
                    .member(member).build();
            teamMember.addTeamMember(team, member);
            teamMemberRepository.save(teamMember);
        } else {
            // TODO 중복 회원 저장 예외
        }
        return team.getId();
    }

    @Transactional
    @Override
    public Long updateTeam(Long teamId, TeamDto updateTeamDto) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomAlertException(TEAM_NOT_FOUND));
        team.updateTeamName(updateTeamDto.getName());
        return team.getId();
    }

    @Transactional
    @Override
    public void deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomAlertException(TEAM_NOT_FOUND));

        List<TeamMember> teamMembers = team.getTeamMembers();
        for (TeamMember teamMember : teamMembers) {
            teamMemberRepository.delete(teamMember);
        }
        teamRepository.delete(team);
    }

    @Transactional
    @Override
    public void removeMember(Long teamId, String username) {
        TeamMember teamMember = teamMemberRepository.findTeamMemberByMemberUsernameAndTeamId(username, teamId)
                .orElseThrow(() -> new CustomAlertException(MEMBER_NOT_FOUND));
        teamMember.removeTeam();
        teamMemberRepository.delete(teamMember);
    }

    @Override
    public boolean validateMemberDuplication(Team team, Member member) {
        List<TeamMember> teamMembers = team.getTeamMembers();
        for (TeamMember teamMember : teamMembers) {
            if (teamMember.getMember().equals(member)) return false;
        }
        return true;
    }

    @Override
    public boolean isTeamLeader(Long teamId, String username) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomAlertException(TEAM_NOT_FOUND));
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomAlertException(MEMBER_NOT_FOUND));
        return team.getTeamLeader() != null && team.getTeamLeader().equals(member);
    }
}
