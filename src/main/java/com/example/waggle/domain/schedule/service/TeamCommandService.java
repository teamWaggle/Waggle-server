package com.example.waggle.domain.schedule.service;

import com.example.waggle.web.dto.schedule.TeamDto;

public interface TeamCommandService {

    Long createTeam(TeamDto teamDto, String username);

    Long addMember(Long teamId, String username);

    Long updateTeam(Long teamId, TeamDto updateTeamDto);

    void deleteTeam(Long teamId);

    void removeMember(Long teamId, String username);

}
