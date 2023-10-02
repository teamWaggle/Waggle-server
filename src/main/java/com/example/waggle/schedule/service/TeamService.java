package com.example.waggle.schedule.service;

import com.example.waggle.member.domain.Member;
import com.example.waggle.member.dto.MemberSummaryDto;
import com.example.waggle.schedule.domain.Team;
import com.example.waggle.schedule.dto.TeamDto;

import java.util.List;

public interface TeamService {
    List<TeamDto> getTeams();

    List<TeamDto> getTeamsByUsername(String username);

    TeamDto getTeamById(Long teamId);

    List<MemberSummaryDto> getTeamMembers(Long teamId);

    Long createTeam(TeamDto teamDto, String username);

    Long addMember(Long teamId, String username);

    Long updateTeam(Long teamId, TeamDto updateTeamDto);

    void deleteTeam(Long teamId);

    void removeMember(Long teamId, String username);

    boolean validateMemberDuplication(Team team, Member member);

    boolean isTeamLeader(Long teamId, String username);
}
