package com.example.waggle.domain.schedule.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.entity.TeamMember;
import com.example.waggle.domain.schedule.repository.ScheduleRepository;
import com.example.waggle.global.exception.handler.ScheduleHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
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
    private final MemberQueryService memberQueryService;

    @Override
    public Schedule getScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
    }

    @Override
    public List<Schedule> getSchedulesByTeamId(Long teamId) {
        return scheduleRepository.findAllByTeamId(teamId);
    }

    @Override
    public List<Schedule> getSchedulesByMemberUsername(String username) {
        Member member = memberQueryService.getMemberByUsername(username);

        return member.getTeamMembers().stream()
                .map(TeamMember::getTeam)
                .flatMap(team -> team.getSchedules().stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<Schedule> getSchedulesByMemberId(Long memberId) {
        Member member = memberQueryService.getMemberById(memberId);
        return member.getTeamMembers().stream()
                .map(TeamMember::getTeam)
                .flatMap(team -> team.getSchedules().stream())
                .collect(Collectors.toList());
    }


    @Override
    public List<Schedule> getMonthlySchedulesByMemberId(Long memberId, int year, int month) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDateTime startDateTime = startOfMonth.atStartOfDay();
        LocalDateTime endDateTime = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth()).atTime(23, 59, 59);

        return scheduleRepository.findSchedulesByMemberIdAndMonth(memberId, startDateTime, endDateTime);

    }

}
