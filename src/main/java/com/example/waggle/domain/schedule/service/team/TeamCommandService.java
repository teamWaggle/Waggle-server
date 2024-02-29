package com.example.waggle.domain.schedule.service.team;

import com.example.waggle.web.dto.schedule.TeamRequest;
import com.example.waggle.domain.member.entity.Member;

public interface TeamCommandService {

    Long createTeam(TeamRequest createTeamRequest, Member member);

    Long updateTeam(Long teamId, TeamRequest updateTeamRequest, Member member);

    void deleteTeam(Long teamId, Member member);

    Long addTeamMember(Long teamId, Long newMemberId, Member member);

    void deleteTeamMemberByLeader(Long teamId, Long memberId, Member leader);

    void deleteTeamMemberByMyself(Long teamId, Member member);

    void changeTeamLeader(Long teamId, Long newLeaderId, Member leader);

    void requestParticipation(Long teamId, Member member);

    void respondToParticipation(Long teamId, Long memberId, Member leader, boolean accept);

}
