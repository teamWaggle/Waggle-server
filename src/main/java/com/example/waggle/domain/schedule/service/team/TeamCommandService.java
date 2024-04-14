package com.example.waggle.domain.schedule.service.team;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.notification.entity.alarm.AlarmEvent;
import com.example.waggle.web.dto.schedule.TeamRequest;

public interface TeamCommandService {

    Long createTeam(TeamRequest createTeamRequest, Member member);

    Long updateTeam(Long teamId, TeamRequest updateTeamRequest, Member member);

    void deleteTeam(Long teamId, Member member);

    void deleteTeamMemberByLeader(Long teamId, Long memberId, Member leader);

    void deleteTeamMemberByMyself(Long teamId, Member member);

    void changeTeamLeader(Long teamId, Long newLeaderId, Member leader);

    AlarmEvent requestParticipation(Long teamId, Member member);

    void respondToParticipation(Long teamId, Long memberId, Member leader, boolean accept);

}
