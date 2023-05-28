package com.example.waggle.repository.team;

import com.example.waggle.domain.team.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

}
