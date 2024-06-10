package com.example.waggle.domain.schedule.persistence.dao.jpa;

import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.schedule.persistence.entity.Participation;
import com.example.waggle.domain.schedule.persistence.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    boolean existsByTeamAndMember(Team team, Member member);

    Optional<Participation> findByTeamAndMember(Team team, Member member);

    Optional<Participation> findByTeamIdAndMemberId(Long teamId, Long memberId);

    void deleteAllByMember(Member member);

    void deleteByMemberAndTeam(Member member, Team team);

    void deleteByMemberIdAndTeamId(Long memberId, Long teamId);

    List<Participation> findByTeam(Team team);

    boolean existsByMemberIdAndTeamId(Long memberId, Long teamId);
}
