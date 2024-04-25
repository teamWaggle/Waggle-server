package com.example.waggle.domain.schedule.repository;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.schedule.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findAllByLeader(Member leader);

    Page<Team> findByTeamMembers_Member_Username(String username, Pageable pageable);

    Page<Team> findByTeamMembers_MemberId(Long memberId, Pageable pageable);

    List<Team> findListByTeamMembers_Member_Username(String username);

    Page<Team> findByNameContaining(String name, Pageable pageable);

    @Query("SELECT t FROM Team t LEFT JOIN t.teamMembers tm GROUP BY t.id ORDER BY COUNT(tm) DESC")
    Page<Team> findBySizeOfTeamMembers(Pageable pageable);   //TODO QUERYDSL

}
