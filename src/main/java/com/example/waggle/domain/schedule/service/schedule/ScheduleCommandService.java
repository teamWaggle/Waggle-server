package com.example.waggle.domain.schedule.service.schedule;

import com.example.waggle.web.dto.schedule.ScheduleRequest;

public interface ScheduleCommandService {

    Long createSchedule(Long teamId, ScheduleRequest createScheduleRequest, String username);

    Long updateSchedule(Long scheduleId, ScheduleRequest updateScheduleRequest);

    Long updateScheduleByUsername(Long scheduleId, String username, ScheduleRequest updateScheduleRequest);

    void deleteSchedule(Long scheduleId);

    void deleteScheduleByUsername(Long scheduleId, String username);

    void deleteScheduleForHardReset(Long scheduleId);

    Long addMemberSchedule(Long scheduleId, String username);

    void deleteMemberSchedule(Long scheduleId, String username);

}
