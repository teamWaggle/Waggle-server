package com.example.waggle.domain.schedule.service;

import com.example.waggle.domain.schedule.dto.ScheduleDto;

import java.util.List;

public interface ScheduleQueryService {

    ScheduleDto getScheduleById(Long scheduleId);

    List<ScheduleDto> getSchedulesByTeamId(Long teamId);

}
