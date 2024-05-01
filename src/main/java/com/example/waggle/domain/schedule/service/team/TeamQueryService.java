package com.example.waggle.domain.schedule.service.team;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.schedule.entity.Participation;
import com.example.waggle.domain.schedule.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TeamQueryService {

    Page<Team> getPagedTeamByMemberId(Long memberId, Pageable pageable);

    Page<Team> getTeamByContainName(String name, Pageable pageable);

    Page<Team> getPopularTeamListTop3(Pageable pageable);

    Team getTeamById(Long teamId);

    List<Participation> getParticipationList(Member leader, Long teamId);

    Optional<Participation> getParticipation(Member member, Long teamId);

    boolean isMemberOfTeam(Member member, Long teamId);

}
