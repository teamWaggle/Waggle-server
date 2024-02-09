package com.example.waggle.domain.schedule.repository;

import com.example.waggle.domain.schedule.entity.MemberSchedule;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberScheduleRepository extends JpaRepository<MemberSchedule, Long> {
    List<MemberSchedule> findByMemberId(Long memberId);

    @Query("SELECT ms FROM MemberSchedule ms JOIN ms.member m JOIN ms.schedule s WHERE ms.member.id = :memberId AND (s.startTime <= :endOfDay AND s.endTime >= :startOfDay)")
    List<MemberSchedule> findByMemberIdAndDay(@Param("memberId") Long memberId,
                                              @Param("startOfDay") LocalDateTime startOfDay,
                                              @Param("endOfDay") LocalDateTime endOfDay);

    List<MemberSchedule> findByScheduleId(Long scheduleId);

    void deleteByMemberIdAndScheduleId(Long memberId, Long scheduleId);

    void deleteAllByScheduleId(Long scheduleId);

}
