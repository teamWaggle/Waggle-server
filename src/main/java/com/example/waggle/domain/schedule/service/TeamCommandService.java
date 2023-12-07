package com.example.waggle.domain.schedule.service;

import com.example.waggle.web.dto.schedule.TeamRequest;

public interface TeamCommandService {

    Long createTeam(TeamRequest.TeamRequestDto request);

    Long updateTeam(Long teamId, TeamRequest.TeamRequestDto request);

    void deleteTeam(Long teamId);

    Long addTeamMember(Long teamId, String username);

    void deleteTeamMember(Long teamId, String username);

    void changeTeamLeader(Long teamId, String username);

}
