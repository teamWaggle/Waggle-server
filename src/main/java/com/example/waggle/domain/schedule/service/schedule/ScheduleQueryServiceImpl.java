package com.example.waggle.domain.schedule.service.schedule;

import com.example.waggle.domain.schedule.entity.MemberSchedule;
import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.repository.MemberScheduleRepository;
import com.example.waggle.domain.schedule.repository.ScheduleRepository;
import com.example.waggle.global.exception.handler.ScheduleHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScheduleQueryServiceImpl implements ScheduleQueryService {

    private final ScheduleRepository scheduleRepository;
    private final MemberScheduleRepository memberScheduleRepository;

    @Override
    public Schedule getScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
    }

    @Override
    public List<Schedule> getTeamSchedules(Long teamId) {
        return scheduleRepository.findAllByTeamId(teamId);
    }

    @Override
    public Page<Schedule> getPagedTeamSchedules(Long teamId, Pageable pageable) {
        return scheduleRepository.findPagedByTeamId(teamId, pageable);
    }

    @Override
    public List<Schedule> getSchedulesByWriter(Long memberId) {
        return scheduleRepository.findListByMemberId(memberId);
    }

    @Override
    public List<Schedule> getSchedulesByMember(Long memberId) {
        return memberScheduleRepository.findByMemberId(memberId).stream()
                .map(MemberSchedule::getSchedule)
                .collect(Collectors.toList());
    }

    @Override
    public List<Schedule> getMonthlySchedulesByMember(Long memberId, int year, int month) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDateTime startDateTime = startOfMonth.atStartOfDay();
        LocalDateTime endDateTime = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth()).atTime(23, 59, 59);

        return memberScheduleRepository.findByMemberIdAndDay(memberId, startDateTime, endDateTime).stream()
                .map(MemberSchedule::getSchedule)
                .collect(Collectors.toList());
    }

    @Override
    public List<Schedule> getMonthlyTeamSchedule(Long teamId, int year, int month) {
        LocalDate startDay = LocalDate.of(year, month, 1);
        LocalDateTime startDateTime = startDay.atStartOfDay();
        LocalDateTime endDateTime = startDay.plusMonths(1).atStartOfDay();

        return scheduleRepository.findByTeamIdAndDay(teamId, startDateTime, endDateTime);
    }

}
