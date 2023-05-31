package com.example.waggle.repository.team;

import com.example.waggle.domain.team.ScheduleMember;
import com.example.waggle.domain.team.Team;
import com.example.waggle.domain.team.TeamMember;
import org.springframework.data.domain.Example;
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
