package com.example.waggle.domain.schedule.repository;

import com.example.waggle.domain.schedule.entity.Schedule;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByTeamId(Long teamId);

    List<Schedule> findListByMemberUsername(String username);

    List<Schedule> findListByMemberId(Long memberId);

    @Query("SELECT s FROM Schedule s JOIN s.team t WHERE s.team.id = :teamId AND (s.startTime <= :endOfDay AND s.endTime >= :startOfDay)")
    List<Schedule> findByTeamIdAndDay(@Param("teamId") Long teamId,
                                      @Param("startOfDay") LocalDateTime startOfDay,
                                      @Param("endOfDay") LocalDateTime endOfDay);

    void deleteAllByMemberUsername(String username);

    void deleteAllByTeamId(Long teamId);


}
