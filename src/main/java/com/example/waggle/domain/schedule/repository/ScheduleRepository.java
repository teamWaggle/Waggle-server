package com.example.waggle.domain.schedule.repository;

import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByTeamId(Long teamId);

    List<Schedule> findListByMemberId(Long memberId);

    @Query("SELECT s FROM Schedule s JOIN s.team t WHERE s.team.id = :teamId AND (s.startDate <= :endOfDay AND s.endDate >= :startOfDay)")
    List<Schedule> findByTeamIdAndDay(@Param("teamId") Long teamId,
                                      @Param("startOfDay") LocalDate startOfDay,
                                      @Param("endOfDay") LocalDate endOfDay);

    Page<Schedule> findPagedByTeamId(Long teamId, Pageable pageable);


    List<Schedule> findAllByTeam(Team team);


}
