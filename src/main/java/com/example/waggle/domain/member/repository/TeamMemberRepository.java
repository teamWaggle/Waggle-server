package com.example.waggle.domain.member.repository;

import com.example.waggle.domain.member.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    Optional<TeamMember> findTeamMemberByMemberUsernameAndTeamId(String username, Long teamId);
    List<TeamMember> findTeamMemberByMemberUsername(String username);
}
