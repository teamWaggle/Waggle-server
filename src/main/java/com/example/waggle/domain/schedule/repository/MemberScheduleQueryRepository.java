package com.example.waggle.domain.schedule.repository;

import com.example.waggle.domain.member.entity.Member;

import java.time.LocalDate;
import java.time.LocalTime;

public interface MemberScheduleQueryRepository {
    Long countOverlappedSchedule(Member member,
                                 Long scheduleId,
                                 LocalDate startDate,
                                 LocalTime startTime,
                                 LocalDate endDate,
                                 LocalTime endTime);
}
