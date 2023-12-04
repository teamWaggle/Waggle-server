package com.example.waggle.domain.schedule.service;

import com.example.waggle.web.dto.schedule.TeamDto;
import java.util.List;

public interface TeamQueryService {

    List<TeamDto> getTeams();

    List<TeamDto> getTeamsByUsername(String username);

    TeamDto getTeamById(Long teamId);

//    List<MemberSummaryDto> getTeamMembers(Long teamId);

    boolean isTeamLeader(Long teamId, String username);

}
