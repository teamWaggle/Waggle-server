package com.example.waggle.schedule.service;

import com.example.waggle.schedule.dto.ScheduleDto;
import java.util.List;

public interface ScheduleQueryService {

    ScheduleDto getScheduleById(Long scheduleId);

    List<ScheduleDto> getSchedulesByTeamId(Long teamId);

}
