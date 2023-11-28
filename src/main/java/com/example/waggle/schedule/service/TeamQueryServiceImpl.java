package com.example.waggle.schedule.service;

import static com.example.waggle.commons.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.example.waggle.commons.exception.ErrorCode.TEAM_NOT_FOUND;

import com.example.waggle.commons.exception.CustomAlertException;
import com.example.waggle.member.domain.Member;
import com.example.waggle.member.domain.TeamMember;
import com.example.waggle.member.dto.MemberSummaryDto;
import com.example.waggle.member.repository.MemberRepository;
import com.example.waggle.member.repository.TeamMemberRepository;
import com.example.waggle.schedule.domain.Team;
import com.example.waggle.schedule.dto.TeamDto;
import com.example.waggle.schedule.repository.TeamRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TeamQueryServiceImpl implements TeamQueryService {

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
    public boolean isTeamLeader(Long teamId, String username) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomAlertException(TEAM_NOT_FOUND));
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomAlertException(MEMBER_NOT_FOUND));
        return team.getTeamLeader() != null && team.getTeamLeader().equals(member);
    }
}
