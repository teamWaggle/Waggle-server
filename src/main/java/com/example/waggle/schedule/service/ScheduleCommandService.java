package com.example.waggle.schedule.service;

import com.example.waggle.schedule.dto.ScheduleDto;

public interface ScheduleCommandService {

    Long createSchedule(ScheduleDto scheduleDto, Long teamId);

    Long updateSchedule(ScheduleDto scheduleDto);

    void deleteSchedule(Long scheduleId);

}
