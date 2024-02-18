package com.example.waggle.domain.schedule.repository;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.entity.Team;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByTeamId(Long teamId);

    List<Schedule> findListByMemberUsername(String username);

    List<Schedule> findListByMemberId(Long memberId);

    @Query("SELECT s FROM Schedule s JOIN s.team t WHERE s.team.id = :teamId AND (s.startTime <= :endOfDay AND s.endTime >= :startOfDay)")
    List<Schedule> findByTeamIdAndDay(@Param("teamId") Long teamId,
                                      @Param("startOfDay") LocalDateTime startOfDay,
                                      @Param("endOfDay") LocalDateTime endOfDay);

    Page<Schedule> findPagedByTeamId(Long teamId, Pageable pageable);

    void deleteAllByMemberUsername(String username);

    void deleteAllByTeamId(Long teamId);

    List<Schedule> findAllByTeam(Team team);

    List<Schedule> findAllByMember(Member member);


}
