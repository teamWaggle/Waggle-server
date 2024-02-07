package com.example.waggle.web.converter;

import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.web.dto.schedule.ScheduleResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ScheduleConverter {

    public static ScheduleResponse.DetailDto toScheduleResponseDto(Schedule schedule) {
        return ScheduleResponse.DetailDto.builder()
                .scheduleId(schedule.getId())
                .teamId(schedule.getTeam().getId())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime()).build();
    }

    public static ScheduleResponse.ListDto toListDto(Page<Schedule> pagedSchedules) {
        List<ScheduleResponse.DetailDto> schedules = pagedSchedules.stream()
                .map(ScheduleConverter::toScheduleResponseDto).collect(Collectors.toList());
        return ScheduleResponse.ListDto.builder()
                .scheduleList(schedules)
                .totalStories(pagedSchedules.getTotalElements())
                .isFirst(pagedSchedules.isFirst())
                .isLast(pagedSchedules.isLast())
                .build();
    }

    public static ScheduleResponse.ListDto toListDto(List<Schedule> schedules) {
        return ScheduleResponse.ListDto.builder()
                .scheduleList(schedules.stream().map(ScheduleConverter::toScheduleResponseDto).collect(Collectors.toList()))
                .totalStories(schedules.size())
                .build();
    }

}
