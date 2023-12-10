package com.example.waggle.domain.schedule.service;

import com.example.waggle.web.dto.schedule.TeamRequest.Post;

public interface TeamCommandService {

    Long createTeam(Post request);

    Long updateTeam(Long teamId, Post request);

    void deleteTeam(Long teamId);

    Long addTeamMember(Long teamId, String username);

    void deleteTeamMember(Long teamId, String username);

    void changeTeamLeader(Long teamId, String username);

}
