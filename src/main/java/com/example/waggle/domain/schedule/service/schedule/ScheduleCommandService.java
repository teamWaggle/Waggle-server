package com.example.waggle.domain.schedule.service.schedule;

import com.example.waggle.web.dto.schedule.ScheduleRequest.Post;

public interface ScheduleCommandService {

    Long createSchedule(Long teamId, Post request, String username);

    Long updateSchedule(Long scheduleId, Post request);

    Long updateScheduleByUsername(Long scheduleId, String username, Post request);

    void deleteSchedule(Long scheduleId);

    void deleteScheduleByUsername(Long scheduleId, String username);

    void deleteScheduleForHardReset(Long scheduleId);

    Long addMemberSchedule(Long scheduleId, String username);

    void deleteMemberSchedule(Long scheduleId, String username);

}
