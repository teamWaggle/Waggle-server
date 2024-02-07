package com.example.waggle.domain.schedule.service.schedule;

import com.example.waggle.domain.schedule.entity.Schedule;

import java.util.List;

public interface ScheduleQueryService {

    Schedule getScheduleById(Long scheduleId);

    List<Schedule> getTeamSchedules(Long teamId);

    List<Schedule> getSchedulesByWriter(Long memberId);

    List<Schedule> getSchedulesByMember(Long memberId);

    List<Schedule> getMonthlySchedulesByMember(Long memberId, int year, int month);

    List<Schedule> getWeeklySchedulesByMember(Long memberId, int year, int month, int day);

    List<Schedule> getMonthlyTeamSchedule(Long teamId, int year, int month);

    List<Schedule> getWeeklyTeamSchedule(Long teamId, int year, int month, int day);

}
