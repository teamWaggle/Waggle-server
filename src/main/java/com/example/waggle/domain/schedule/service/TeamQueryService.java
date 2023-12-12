package com.example.waggle.domain.schedule.service;

import com.example.waggle.domain.schedule.domain.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamQueryService {

    Page<Team> getTeamsByUsername(String username, Pageable pageable);

    Team getTeamById(Long teamId);

    boolean isTeamLeader(Long teamId, String username);

}
