package com.example.waggle.schedule.repository;

import com.example.waggle.schedule.domain.Schedule;
import com.example.waggle.member.domain.ScheduleMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByTeamId(Long teamId);
    @Query("SELECT sm FROM ScheduleMember sm JOIN sm.member m WHERE m.username IN :usernames")
    List<ScheduleMember> findAllScheduleMembersByUsername(@Param("usernames") List<String> usernames);



}
