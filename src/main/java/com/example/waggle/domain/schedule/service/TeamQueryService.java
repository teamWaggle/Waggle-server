package com.example.waggle.domain.schedule.service;

import com.example.waggle.domain.schedule.domain.Team;
import java.util.List;

public interface TeamQueryService {

    List<Team> getTeamsByUsername(String username);

    Team getTeamById(Long teamId);

    boolean isTeamLeader(Long teamId, String username);

}
