package com.example.waggle.domain.schedule.service.schedule;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.schedule.entity.MemberSchedule;
import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.repository.MemberScheduleRepository;
import com.example.waggle.domain.schedule.repository.ScheduleRepository;
import com.example.waggle.global.exception.handler.ScheduleHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.schedule.ScheduleResponse.ScheduleDetailDto;
import com.example.waggle.web.dto.schedule.ScheduleResponse.ScheduleListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
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
    public List<Schedule> getSchedulesByOwner(Long memberId) {
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
    public List<Schedule> getTeamScheduleByPeriod(Long teamId, LocalDate startDate, LocalDate endDate) {
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
    public HashMap<Long, Boolean> getMapOfIsScheduled(Member member, ScheduleListDto scheduleListDto) {
        HashMap<Long, Boolean> isScheduledMap = new HashMap<>();
        scheduleListDto.getScheduleList().stream()
                .map(ScheduleDetailDto::getBoardId)
                .forEach(boardId ->
                        isScheduledMap.put(boardId, memberScheduleRepository.existsByMemberIdAndScheduleId(member.getId(), boardId))
                );
        return isScheduledMap;
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
                schedule.getEndTime(),
                schedule.getId()
        );
    }

    //사실 scheduleListDto안에 바로 값을 넣어주는게 더 깔끔할 것 같은데
    //수경님이 예전에 그런식으로 짜는 걸 별로 안좋아하시는 것 같아서 일단 이렇게 짜봤습니다
    //뭐가 나을지 코멘트 부탁드려요
    @Override
    public HashMap<Long, Long> getMapOfOverlappedScheduleCount(Member member, ScheduleListDto scheduleListDto) {
        HashMap<Long, Long> countHashMap = new HashMap<>();
        List<Long> boardIdList = scheduleListDto.getScheduleList().stream()
                .map(ScheduleDetailDto::getBoardId)
                .collect(Collectors.toList());
        List<Schedule> scheduleList = scheduleRepository.findAllById(boardIdList);
        scheduleList.stream()
                .forEach(schedule -> countHashMap.put(schedule.getId(),
                        memberScheduleRepository.countOverlappingSchedules(
                                member.getId(),
                                schedule.getStartDate(),
                                schedule.getEndDate(),
                                schedule.getStartTime(),
                                schedule.getEndTime(),
                                schedule.getId()
                        )
                ));
        return countHashMap;
    }

}
