package com.example.waggle.schedule.service;

import com.example.waggle.schedule.dto.ScheduleDto;

import java.util.List;

public interface ScheduleService {

    ScheduleDto getScheduleById(Long scheduleId);

    List<ScheduleDto> getSchedulesByTeamId(Long teamId);

    Long createSchedule(ScheduleDto scheduleDto, Long teamId);

    Long updateSchedule(ScheduleDto scheduleDto);

    void deleteSchedule(Long scheduleId);
}
