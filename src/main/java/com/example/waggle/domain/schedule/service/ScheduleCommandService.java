package com.example.waggle.domain.schedule.service;

import com.example.waggle.domain.schedule.dto.ScheduleDto;

public interface ScheduleCommandService {

    Long createSchedule(ScheduleDto scheduleDto, Long teamId);

    Long updateSchedule(ScheduleDto scheduleDto);

    void deleteSchedule(Long scheduleId);

}
