package com.example.waggle.domain.schedule.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.schedule.domain.TeamMember;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.schedule.repository.TeamMemberRepository;
import com.example.waggle.domain.schedule.domain.Team;
import com.example.waggle.domain.schedule.repository.TeamRepository;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.exception.handler.TeamHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.schedule.TeamDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

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
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        return TeamDto.toDto(team);
    }

//    @Override
//    public List<MemberSummaryDto> getTeamMembers(Long teamId) {
//        Team team = teamRepository.findById(teamId)
//                .orElseThrow(() -> new CustomAlertException(TEAM_NOT_FOUND));
//
//        return team.getTeamMembers().stream()
//                .map(TeamMember::getMember)
//                .map(MemberSummaryDto::toDto)
//                .collect(Collectors.toList());
//    }

    @Override
    public boolean isTeamLeader(Long teamId, String username) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        return team.getTeamLeader() != null && team.getTeamLeader().equals(member);
    }
}
