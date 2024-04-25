package com.example.waggle.domain.schedule.repository;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.schedule.entity.Schedule;

import java.util.List;

public interface MemberScheduleQueryRepository {
    Long countOverlappedSchedule(Member member, Schedule schedule);

    List<Schedule> findOverlappingScheduleList(Member member, Schedule schedule);
}
