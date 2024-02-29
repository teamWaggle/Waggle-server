package com.example.waggle.web.converter;

import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.global.util.ScheduleUtil;
import com.example.waggle.global.util.SecurityUtil;
import com.example.waggle.web.dto.schedule.ScheduleResponse.ScheduleDetailDto;
import com.example.waggle.web.dto.schedule.ScheduleResponse.ScheduleListDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ScheduleConverter {

    public static ScheduleDetailDto toScheduleDetailDto(Schedule schedule) {
        return ScheduleDetailDto.builder()
                .boardId(schedule.getId())
                .teamId(schedule.getTeam().getId())
                .teamColor(schedule.getTeam().getTeamColor())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .createdDate(schedule.getCreatedDate())
                .status(ScheduleUtil.setStatus(schedule))
                .member(MemberConverter.toMemberSummaryDto(schedule.getMember()))
                .isOwner(schedule.getMember().getUsername().equals(SecurityUtil.getCurrentUsername()))
                .build();
    }

    public static ScheduleListDto toScheduleListDto(Page<Schedule> pagedSchedules) {
        List<ScheduleDetailDto> schedules = pagedSchedules.stream()
                .map(ScheduleConverter::toScheduleDetailDto).collect(Collectors.toList());
        return ScheduleListDto.builder()
                .scheduleList(schedules)
                .scheduleCount(pagedSchedules.getTotalElements())
                .isFirst(pagedSchedules.isFirst())
                .isLast(pagedSchedules.isLast())
                .build();
    }

    public static ScheduleListDto toScheduleListDto(List<Schedule> schedules) {
        return ScheduleListDto.builder()
                .scheduleList(
                        schedules.stream().map(ScheduleConverter::toScheduleDetailDto).collect(Collectors.toList()))
                .scheduleCount(schedules.size())
                .build();
    }

}
