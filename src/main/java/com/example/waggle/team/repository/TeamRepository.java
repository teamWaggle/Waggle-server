package com.example.waggle.team.repository;

import com.example.waggle.team.domain.Team;
import com.example.waggle.member.domain.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByTeamMembers(TeamMember teamMember);
    @Query("SELECT tm FROM TeamMember tm JOIN tm.member m WHERE m.username IN :usernames")
    List<TeamMember> findAllTeamMembersByUsername(@Param("usernames") List<String> usernames);

}
