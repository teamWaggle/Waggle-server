package com.example.waggle.domain.schedule.service.schedule;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.schedule.entity.MemberSchedule;
import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.repository.MemberScheduleRepository;
import com.example.waggle.domain.schedule.repository.ScheduleRepository;
import com.example.waggle.global.exception.handler.ScheduleHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<Schedule> getPagedTeamSchedules(Member member, Long teamId, Pageable pageable) {
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
    public List<Schedule> getMonthlySchedulesByMemberUserUrl(String userUrl, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        return memberScheduleRepository.findByMemberUserUrlAndDay(userUrl, startDate, endDate).stream()
                .map(MemberSchedule::getSchedule)
                .collect(Collectors.toList());
    }

    @Override
    public List<Schedule> getMonthlyTeamSchedule(Long teamId, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        return scheduleRepository.findByTeamIdAndDay(teamId, startDate, endDate);
    }

    @Override
    public List<Schedule> getTeamScheduleByPeriod(Member member, Long teamId, LocalDate startDate, LocalDate endDate) {
        return scheduleRepository.findByTeamIdAndDay(teamId, startDate, endDate);
    }

    @Override
    public List<Member> getMemberBySchedule(Long scheduleId) {
        return memberScheduleRepository.findByScheduleId(scheduleId).stream()
                .map(MemberSchedule::getMember)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean getIsScheduled(Member member, Long scheduleId) {
        return memberScheduleRepository.existsByMemberIdAndScheduleId(member.getId(), scheduleId);
    }

    @Override
    public List<Schedule> findOverlappingSchedules(Member member, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        return memberScheduleRepository.findOverlappingSchedules(
                member.getId(),
                schedule.getStartDate(),
                schedule.getEndDate(),
                schedule.getStartTime(),
                schedule.getEndTime());
    }

}
