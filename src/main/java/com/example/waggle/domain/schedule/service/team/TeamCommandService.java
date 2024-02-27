package com.example.waggle.domain.schedule.service.team;

import com.example.waggle.web.dto.schedule.TeamRequest;

public interface TeamCommandService {

    Long createTeam(TeamRequest createTeamRequest);

    Long createTeam(TeamRequest createTeamRequest, String username);

    Long updateTeam(Long teamId, TeamRequest updateTeamRequest);

    Long updateTeam(Long teamId, String username, TeamRequest updateTeamRequest);

    void deleteTeam(Long teamId);

    void deleteTeam(Long teamId, String username);

    Long addTeamMember(Long teamId, Long memberId);

    void deleteTeamMemberByLeader(Long teamId, Long memberId, String username);

    void deleteTeamMemberByMyself(Long teamId, String username);

    void changeTeamLeader(Long teamId, Long memberId, String leaderUsername);

    void requestParticipation(Long teamId, String username);

    void respondToParticipation(Long teamId, Long memberId, String leaderUsername, boolean accept);

}
