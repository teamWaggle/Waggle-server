package com.example.waggle.domain.schedule.persistence.dao.schedule.querydsl;

import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.schedule.persistence.entity.Schedule;

import java.util.List;

public interface MemberScheduleQueryRepository {
    Long countOverlappedSchedule(Member member, Schedule schedule);

    List<Schedule> findOverlappingScheduleList(Member member, Schedule schedule);
}
