package com.example.waggle.domain.schedule.repository;

import com.example.waggle.domain.schedule.entity.MemberSchedule;
import com.example.waggle.domain.schedule.entity.Schedule;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface MemberScheduleRepository extends JpaRepository<MemberSchedule, Long> {
    List<MemberSchedule> findByMemberId(Long memberId);

    @Query("SELECT ms FROM MemberSchedule ms JOIN ms.member m JOIN ms.schedule s WHERE ms.member.id = :memberId AND (s.startDate <= :endOfDay AND s.endDate >= :startOfDay)")
    List<MemberSchedule> findByMemberIdAndDay(@Param("memberId") Long memberId,
                                              @Param("startOfDay") LocalDate startOfDay,
                                              @Param("endOfDay") LocalDate endOfDay);

    @Query("SELECT ms FROM MemberSchedule ms JOIN ms.member m JOIN ms.schedule s WHERE ms.member.userUrl = :userUrl AND (s.startDate <= :endOfDay AND s.endDate >= :startOfDay)")
    List<MemberSchedule> findByMemberUserUrlAndDay(@Param("userUrl") String userUrl,
                                                   @Param("startOfDay") LocalDate startOfDay,
                                                   @Param("endOfDay") LocalDate endOfDay);

    List<MemberSchedule> findByScheduleId(Long scheduleId);

    void deleteByMemberIdAndScheduleId(Long memberId, Long scheduleId);

    void deleteAllByScheduleId(Long scheduleId);

    void deleteAllByMemberId(Long memberId);

    boolean existsByMemberIdAndScheduleId(Long memberId, Long scheduleId);

    void deleteAllBySchedule(Schedule schedule);

}
