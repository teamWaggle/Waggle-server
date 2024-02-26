package com.example.waggle.domain.schedule.service.schedule;

import com.example.waggle.web.dto.schedule.ScheduleRequest.ScheduleCreateDto;

public interface ScheduleCommandService {

    Long createSchedule(Long teamId, ScheduleCreateDto request, String username);

    Long updateSchedule(Long scheduleId, ScheduleCreateDto request);

    Long updateScheduleByUsername(Long scheduleId, String username, ScheduleCreateDto request);

    void deleteSchedule(Long scheduleId);

    void deleteScheduleByUsername(Long scheduleId, String username);

    void deleteScheduleForHardReset(Long scheduleId);

    Long addMemberSchedule(Long scheduleId, String username);

    void deleteMemberSchedule(Long scheduleId, String username);

}
