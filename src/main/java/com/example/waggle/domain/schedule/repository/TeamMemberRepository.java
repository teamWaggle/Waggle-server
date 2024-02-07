package com.example.waggle.domain.schedule.repository;


import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.schedule.entity.Team;
import com.example.waggle.domain.schedule.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    Optional<TeamMember> findTeamMemberByMemberUsernameAndTeamId(String username, Long teamId);

    List<TeamMember> findTeamMemberByTeamId(Long teamId);

    boolean existsByMemberUsername(String username);

    boolean existsByMemberAndTeam(Member member, Team team);

    void deleteAllByMemberUsername(String username);

    void deleteAllByMemberUsernameAndTeamId(String username, Long teamId);

}
