package com.example.waggle.domain.schedule.service;

import com.example.waggle.web.dto.schedule.TeamRequest;

public interface TeamCommandService {

    Long createTeam(TeamRequest.TeamRequestDto request);

    Long updateTeam(Long teamId, TeamRequest.TeamRequestDto request);

    boolean deleteTeam(Long teamId);

    Long addTeamMember(Long teamId, String username);

    boolean deleteTeamMember(Long teamId, String username);

    boolean changeTeamLeader(Long teamId, String username);

}
