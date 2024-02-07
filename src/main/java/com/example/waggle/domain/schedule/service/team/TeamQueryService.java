package com.example.waggle.domain.schedule.service.team;

import com.example.waggle.domain.schedule.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TeamQueryService {

    Page<Team> getPagingTeamByUsername(String username, Pageable pageable);

    List<Team> getTeamListByUsername(String username);

    Team getTeamById(Long teamId);

    boolean isTeamLeader(Long teamId, String username);

}
