package com.example.waggle.domain.schedule.service;

import com.example.waggle.domain.schedule.domain.Schedule;
import com.example.waggle.domain.schedule.repository.ScheduleRepository;
import com.example.waggle.global.exception.handler.ScheduleHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.schedule.ScheduleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleQueryServiceImpl implements ScheduleQueryService {

    private final ScheduleRepository scheduleRepository;

    @Override
    public ScheduleDto getScheduleById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        return ScheduleDto.toDto(schedule);
    }

    @Override
    public List<ScheduleDto> getSchedulesByTeamId(Long teamId) {
        List<Schedule> result = scheduleRepository.findAllByTeamId(teamId);
        return result.stream().map(ScheduleDto::toDto).collect(Collectors.toList());
    }
}
