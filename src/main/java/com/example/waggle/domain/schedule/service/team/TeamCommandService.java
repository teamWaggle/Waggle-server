package com.example.waggle.domain.schedule.service.team;

import com.example.waggle.web.dto.schedule.TeamRequest.Post;

public interface TeamCommandService {

    Long createTeam(Post request);

    Long createTeam(Post request, String username);

    Long updateTeam(Long teamId, Post request);

    Long updateTeam(Long teamId, String username, Post request);

    void deleteTeam(Long teamId);

    void deleteTeam(Long teamId, String username);

    Long addTeamMember(Long teamId, Long memberId);

    void deleteTeamMemberByLeader(Long teamId, Long memberId, String username);

    void deleteTeamMemberByMyself(Long teamId, String username);

    void changeTeamLeader(Long teamId, Long memberId, String leaderUsername);

    void requestParticipation(Long teamId, String username);

    void respondToParticipation(Long teamId, Long memberId, String leaderUsername, boolean accept);

}
