package com.example.waggle.domain.schedule.service;

import com.example.waggle.domain.schedule.domain.Schedule;
import com.example.waggle.domain.schedule.domain.Team;
import com.example.waggle.domain.schedule.repository.ScheduleRepository;
import com.example.waggle.domain.schedule.repository.TeamRepository;
import com.example.waggle.global.exception.handler.ScheduleHandler;
import com.example.waggle.global.exception.handler.TeamHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.schedule.ScheduleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ScheduleCommandServiceImpl implements ScheduleCommandService{

    private final ScheduleRepository scheduleRepository;
    private final TeamRepository teamRepository;


    @Override
    public Long createSchedule(Long teamId, ScheduleRequest.ScheduleRequestDto request) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        Schedule createdSchedule = Schedule.builder()
                .team(team)
                .title(request.getTitle())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();

        Schedule schedule = scheduleRepository.save(createdSchedule);
        return schedule.getId();
    }

    @Override
    public Long updateSchedule(Long scheduleId, ScheduleRequest.ScheduleRequestDto request) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        schedule.update(request);

        return schedule.getId();
    }

    @Override
    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        scheduleRepository.delete(schedule);
    }
}
