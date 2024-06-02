package com.example.waggle.domain.schedule.persistence.dao.jpa;


import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.schedule.persistence.entity.Team;
import com.example.waggle.domain.schedule.persistence.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    boolean existsByMemberAndTeam(Member member, Team team);

    void deleteAllByMemberIdAndTeamId(Long memberId, Long teamId);

    List<TeamMember> findAllByMember(Member member);

    void deleteAllByTeam(Team team);

    int countByTeam(Team team);
}
