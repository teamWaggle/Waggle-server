package com.example.waggle.domain.schedule.repository;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.schedule.entity.Participation;
import com.example.waggle.domain.schedule.entity.Team;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    boolean existsByTeamAndMember(Team team, Member member);

    Optional<Participation> findByTeamAndMember(Team team, Member member);

    Optional<Participation> findByTeamIdAndMemberId(Long teamId, Long memberId);

    void deleteAllByMember(Member member);

    void deleteByMemberAndTeam(Member member, Team team);

    List<Participation> findByTeam(Team team);

}
