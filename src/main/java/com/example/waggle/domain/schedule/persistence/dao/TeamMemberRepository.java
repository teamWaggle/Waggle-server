package com.example.waggle.domain.schedule.persistence.dao;


import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.schedule.persistence.entity.Team;
import com.example.waggle.domain.schedule.persistence.entity.TeamMember;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    boolean existsByMemberAndTeam(Member member, Team team);

    void deleteAllByMemberIdAndTeamId(Long memberId, Long teamId);

    List<TeamMember> findAllByMember(Member member);

    void deleteAllByTeam(Team team);

}