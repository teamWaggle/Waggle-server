package com.example.waggle.domain.schedule.service;

import com.example.waggle.web.dto.schedule.ScheduleRequest;

public interface ScheduleCommandService {

    Long createSchedule(Long teamId, ScheduleRequest.ScheduleRequestDto request);

    Long updateSchedule(Long scheduleId, ScheduleRequest.ScheduleRequestDto request);

    void deleteSchedule(Long scheduleId);

}
