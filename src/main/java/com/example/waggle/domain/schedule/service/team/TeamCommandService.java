package com.example.waggle.domain.schedule.service.team;

import com.example.waggle.web.dto.schedule.TeamRequest.Post;

public interface TeamCommandService {

    Long createTeam(Post request);

    Long createTeam(Post request, String username);

    Long updateTeam(Long teamId, Post request);

    void deleteTeam(Long teamId);

    Long addTeamMember(Long teamId, String username);

    void deleteTeamMember(Long teamId, String username);

    void changeTeamLeader(Long teamId, String username);

    void requestParticipation(Long teamId, String username);

    void respondToParticipation(Long teamId, Long memberId, boolean accept);

}
