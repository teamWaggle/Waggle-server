package com.example.waggle.domain.schedule.service.schedule;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleQueryService {

    Schedule getScheduleById(Long scheduleId);

    List<Schedule> getTeamSchedules(Long teamId);

    Page<Schedule> getPagedTeamSchedules(Long teamId, Pageable pageable);

    List<Schedule> getSchedulesByWriter(Long memberId);

    List<Schedule> getSchedulesByMember(Long memberId);

    List<Schedule> getMonthlySchedulesByMember(Long memberId, int year, int month);

    List<Schedule> getMonthlyTeamSchedule(Long teamId, int year, int month);

    List<Schedule> getTeamScheduleByPeriod(Long teamId, LocalDate startTime, LocalDate endTime);

    List<Member> getMemberBySchedule(Long scheduleId);

}
