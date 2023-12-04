package com.example.waggle.domain.schedule.service;

import static com.example.waggle.global.exception.ErrorCode.SCHEDULE_NOT_FOUND;

import com.example.waggle.domain.schedule.domain.Schedule;
import com.example.waggle.domain.schedule.repository.ScheduleRepository;
import com.example.waggle.web.dto.schedule.ScheduleDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleQueryServiceImpl implements ScheduleQueryService {

    private final ScheduleRepository scheduleRepository;

    @Override
    public ScheduleDto getScheduleById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomAlertException(SCHEDULE_NOT_FOUND));
        return ScheduleDto.toDto(schedule);
    }

    @Override
    public List<ScheduleDto> getSchedulesByTeamId(Long teamId) {
        List<Schedule> result = scheduleRepository.findAllByTeamId(teamId);
        return result.stream().map(ScheduleDto::toDto).collect(Collectors.toList());
    }
}
