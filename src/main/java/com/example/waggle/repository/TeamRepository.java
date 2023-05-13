package com.example.waggle.repository;

import com.example.waggle.domain.team.Team;
import com.example.waggle.domain.team.TeamMember;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByTeamMembers(TeamMember teamMember);
}
