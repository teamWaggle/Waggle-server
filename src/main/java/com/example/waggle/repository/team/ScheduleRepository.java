package com.example.waggle.repository.team;

import com.example.waggle.domain.team.Schedule;
import com.example.waggle.domain.team.ScheduleMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByTeamId(Long teamId);
    @Query("SELECT sm FROM ScheduleMember sm JOIN sm.member m WHERE m.username IN :usernames")
    List<ScheduleMember> findAllScheduleMembersByUsername(@Param("usernames") List<String> usernames);



}
