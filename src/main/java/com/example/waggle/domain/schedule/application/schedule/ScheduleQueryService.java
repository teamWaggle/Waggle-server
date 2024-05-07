package com.example.waggle.domain.schedule.application.schedule;

import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.schedule.persistence.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static com.example.waggle.domain.schedule.presentation.dto.schedule.ScheduleResponse.ScheduleListDto;

public interface ScheduleQueryService {

    Schedule getScheduleById(Long scheduleId);

    List<Schedule> getTeamSchedules(Long teamId);

    Page<Schedule> getPagedTeamSchedules(Long teamId, Pageable pageable);

    List<Schedule> getSchedulesByOwner(Long memberId);

    List<Schedule> getSchedulesByMember(Long memberId);

    List<Schedule> getMonthlySchedulesByMemberUserUrl(String userUrl, int year, int month);

    List<Schedule> getMonthlyTeamSchedule(Long teamId, int year, int month);

    List<Schedule> getTeamScheduleByPeriod(Long teamId, LocalDate startTime, LocalDate endTime);

    List<Member> getMemberBySchedule(Long scheduleId);

    Boolean getIsScheduled(Member member, Long boardId);

    HashMap<Long, Boolean> getMapOfIsScheduled(Member member, ScheduleListDto scheduleListDto);

    List<Schedule> findOverlappingSchedules(Member member, Long scheduleId);

    HashMap<Long, Long> getMapOfOverlappedScheduleCount(Member member, ScheduleListDto scheduleListDto);

}
