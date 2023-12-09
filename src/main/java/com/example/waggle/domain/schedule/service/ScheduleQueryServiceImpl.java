package com.example.waggle.domain.schedule.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.schedule.domain.Schedule;
import com.example.waggle.domain.schedule.domain.TeamMember;
import com.example.waggle.domain.schedule.repository.ScheduleRepository;
import com.example.waggle.global.exception.handler.ScheduleHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
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

}
