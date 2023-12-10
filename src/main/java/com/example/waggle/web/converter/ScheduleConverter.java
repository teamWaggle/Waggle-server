package com.example.waggle.web.converter;

import com.example.waggle.domain.schedule.domain.Schedule;
import com.example.waggle.web.dto.schedule.ScheduleResponse;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleConverter {

    public static ScheduleResponse.ScheduleResponseDto toScheduleResponseDto(Schedule schedule) {
        return ScheduleResponse.ScheduleResponseDto.builder()
                .scheduleId(schedule.getId())
                .teamId(schedule.getTeam().getId())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime()).build();
    }

    public static ScheduleResponse.ListDto toListDto(List<Schedule> schedules) {
        return ScheduleResponse.ListDto
                .builder()
                .schedules(schedules.stream().map(ScheduleConverter::toScheduleResponseDto).collect(Collectors.toList()))
                .build();
    }

}