package com.example.waggle.domain.schedule.service;

import com.example.waggle.web.dto.schedule.TeamResponse;
import java.util.List;

public interface TeamQueryService {

    List<TeamResponse.ListDto> getTeamsByUsername(String username);

    TeamResponse.DetailDto getTeamById(Long teamId);

    boolean isTeamLeader(Long teamId, String username);

}
