package com.example.waggle.domain.schedule.repository;

import com.example.waggle.domain.schedule.entity.Schedule;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByTeamId(Long teamId);

    @Query("SELECT s FROM Schedule s JOIN s.team t JOIN t.teamMembers tm WHERE tm.member.username = :username AND (s.startTime <= :endOfMonth AND s.endTime >= :startOfMonth)")
    List<Schedule> findSchedulesByMemberUsernameAndMonth(@Param("username") String username,
                                                         @Param("startOfMonth") LocalDateTime startOfMonth,
                                                         @Param("endOfMonth") LocalDateTime endOfMonth);

    List<Schedule> findListByMemberUsername(String username);

    void deleteAllByMemberUsername(String username);

    void deleteAllByTeamId(Long teamId);


}
