package com.example.waggle.schedule.service;

import com.example.waggle.member.domain.Member;
import com.example.waggle.member.dto.MemberDto;
import com.example.waggle.schedule.domain.Team;
import com.example.waggle.schedule.dto.TeamDto;

import java.util.List;
import java.util.Optional;

public interface TeamService {
    public List<TeamDto> findAllTeam();

    public List<TeamDto> findAllTeamByUsername(String username);

    public Optional<TeamDto> findByTeamId(Long teamId);

    public List<MemberDto> findTeamMembers(Long teamId);

    public TeamDto createTeamWithMember(TeamDto teamDto, String username);

    public TeamDto addMember(Long teamId, String username);

    public TeamDto updateTeam(Long teamId, TeamDto updateTeamDto);

    public Boolean removeTeam(Long teamId);

    public Boolean removeMember(Long teamId, String username);

    public Boolean validateDuplicateMember(Team team, Member member);

    public Boolean isTeamLeader(Long teamId, String username);
}
