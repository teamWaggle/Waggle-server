package com.example.waggle.domain.schedule.persistence.dao.jpa;

import com.example.waggle.domain.schedule.persistence.dao.querydsl.MemberScheduleQueryRepository;
import com.example.waggle.domain.schedule.persistence.entity.MemberSchedule;
import com.example.waggle.domain.schedule.persistence.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MemberScheduleRepository extends JpaRepository<MemberSchedule, Long>, MemberScheduleQueryRepository {
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

    @Query("SELECT ms.schedule FROM MemberSchedule ms WHERE ms.member.id = :memberId AND " +
            "ms.schedule.id <> :excludedScheduleId AND " +
            "(ms.schedule.startDate <= :endDate AND ms.schedule.endDate >= :startDate) AND " +
            "(ms.schedule.startTime < :endTime AND ms.schedule.endTime > :startTime)")
    List<Schedule> findOverlappingSchedules(@Param("memberId") Long memberId,
                                            @Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate,
                                            @Param("startTime") LocalTime startTime,
                                            @Param("endTime") LocalTime endTime,
                                            @Param("excludedScheduleId") Long excludedScheduleId);


}
