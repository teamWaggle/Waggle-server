package com.example.waggle.domain.schedule.repository;

import com.example.waggle.domain.schedule.entity.TeamMember;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    Optional<TeamMember> findTeamMemberByMemberUsernameAndTeamId(String username, Long teamId);

}
