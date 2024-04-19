package com.example.waggle.domain.schedule.service.schedule;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.schedule.entity.Schedule;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleQueryService {

    Schedule getScheduleById(Long scheduleId);

    List<Schedule> getTeamSchedules(Long teamId);

    Page<Schedule> getPagedTeamSchedules(Member member, Long teamId, Pageable pageable);

    List<Schedule> getSchedulesByWriter(Long memberId);

    List<Schedule> getSchedulesByMember(Long memberId);

    List<Schedule> getMonthlySchedulesByMemberUserUrl(String userUrl, int year, int month);

    List<Schedule> getMonthlyTeamSchedule(Long teamId, int year, int month);

    List<Schedule> getTeamScheduleByPeriod(Member member, Long teamId, LocalDate startTime, LocalDate endTime);

    List<Member> getMemberBySchedule(Long scheduleId);

    Boolean getIsScheduled(Member member, Long boardId);

    List<Schedule> findOverlappingSchedules(Member member, Long scheduleId);

}
