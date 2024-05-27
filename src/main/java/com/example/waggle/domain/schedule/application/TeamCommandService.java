package com.example.waggle.domain.schedule.application;

import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.schedule.presentation.dto.team.TeamRequest;

public interface TeamCommandService {

    Long createTeam(TeamRequest createTeamRequest, Member member);

    Long updateTeam(Long teamId, TeamRequest updateTeamRequest, Member member);

    void deleteTeam(Long teamId, Member member);

    void deleteTeamMemberByLeader(Long teamId, Long memberId, Member leader);

    void deleteTeamMemberByMyself(Long teamId, Member member);

    void deleteTeamByAdmin(Long teamId, Member member);

    void changeTeamLeader(Long teamId, Long newLeaderId, Member leader);

    Long requestParticipation(Long teamId, Member member);

    void respondToParticipation(Long teamId, Long memberId, Member leader, boolean accept);

}
