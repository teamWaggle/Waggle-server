package com.example.waggle.schedule.service;

import com.example.waggle.schedule.dto.ScheduleDto;

import java.util.List;

public interface ScheduleService {

    ScheduleDto getScheduleById(Long scheduleId);

    List<ScheduleDto> getSchedulesByTeamId(Long teamId);

    ScheduleDto createSchedule(ScheduleDto scheduleDto, Long teamId);

    ScheduleDto updateSchedule(ScheduleDto scheduleDto);

    void deleteSchedule(Long scheduleId);
}
