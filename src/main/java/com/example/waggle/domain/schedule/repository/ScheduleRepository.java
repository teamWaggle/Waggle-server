package com.example.waggle.domain.schedule.repository;

import com.example.waggle.domain.schedule.domain.Schedule;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByTeamId(Long teamId);

    @Query("SELECT s FROM Schedule s JOIN s.team t JOIN t.teamMembers tm WHERE tm.member.username = :username AND (s.startTime <= :endOfMonth AND s.endTime >= :startOfMonth)")
    List<Schedule> findSchedulesByMemberUsernameAndMonth(@Param("username") String username,
                                                         @Param("startOfMonth") LocalDateTime startOfMonth,
                                                         @Param("endOfMonth") LocalDateTime endOfMonth);


}
