package com.example.waggle.domain.schedule.service.schedule;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.schedule.ScheduleRequest.Post;

public interface ScheduleCommandService {

    Long createSchedule(Long teamId, Member member, Post request);

    Long updateSchedule(Long scheduleId, Post request);

    Long updateSchedule(Long scheduleId, Member member, Post request);

    void deleteSchedule(Long scheduleId);

    void deleteSchedule(Long scheduleId, Member member);

    void deleteScheduleForHardReset(Long scheduleId);

    Long addMemberSchedule(Long scheduleId, Member member);

    void deleteMemberSchedule(Long scheduleId, Member member);

}
