package com.example.waggle.domain.schedule.service.team;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.schedule.TeamRequest.Post;

public interface TeamCommandService {

    Long createTeam(Post request);

    Long createTeam(Member member, Post request);

    Long updateTeam(Long teamId, Post request);

    Long updateTeam(Long teamId, Member member, Post request);

    void deleteTeam(Long teamId);

    void deleteTeam(Long teamId, Member member);

    Long addTeamMember(Long teamId, Member member);

    void deleteTeamMemberByLeader(Long teamId, Long memberId, Member leader);

    void deleteTeamMemberByMyself(Long teamId, Member member);

    void changeTeamLeader(Long teamId, Long memberId, Member member);

    void requestParticipation(Long teamId, Member member);

    void respondToParticipation(Long teamId, Long memberId, Member leader, boolean accept);

}
