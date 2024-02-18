package com.example.waggle.domain.schedule.repository;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.schedule.entity.Team;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findAllByLeader(Member leader);

    Page<Team> findByTeamMembers_Member_Username(String username, Pageable pageable);

    Page<Team> findByTeamMembers_MemberId(Long memberId, Pageable pageable);

    List<Team> findListByTeamMembers_Member_Username(String username);

}
