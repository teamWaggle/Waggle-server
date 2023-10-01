package com.example.waggle.schedule.service;

import com.example.waggle.schedule.dto.ScheduleDto;

import java.util.List;
import java.util.Optional;

public interface ScheduleService {

    public Optional<ScheduleDto> findByScheduleId(Long scheduleId);

    public List<ScheduleDto> findByTeamId(Long teamId);

    public ScheduleDto addSchedule(ScheduleDto scheduleDto, Long teamId);

    public ScheduleDto updateSchedule(ScheduleDto scheduleDto);

    public Boolean removeSchedule(Long scheduleId);
}
