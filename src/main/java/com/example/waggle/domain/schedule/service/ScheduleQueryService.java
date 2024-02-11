package com.example.waggle.domain.schedule.service;

import com.example.waggle.domain.schedule.entity.Schedule;

import java.util.List;

public interface ScheduleQueryService {

    Schedule getScheduleById(Long scheduleId);

    List<Schedule> getSchedulesByTeamId(Long teamId);

    List<Schedule> getSchedulesByMemberUsername(String username);

    List<Schedule> getSchedulesByMemberId(Long memberId);

    List<Schedule> getMonthlySchedulesByMemberId(Long memberId, int year, int month);

}
