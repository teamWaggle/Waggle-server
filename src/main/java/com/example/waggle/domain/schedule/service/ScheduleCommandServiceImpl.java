package com.example.waggle.domain.schedule.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.entity.ScheduleMember;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.schedule.domain.Schedule;
import com.example.waggle.domain.schedule.domain.Team;
import com.example.waggle.domain.schedule.repository.ScheduleRepository;
import com.example.waggle.domain.schedule.repository.TeamRepository;
import com.example.waggle.global.exception.handler.ScheduleHandler;
import com.example.waggle.global.exception.handler.TeamHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.schedule.ScheduleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.waggle.global.exception.ErrorCode.SCHEDULE_NOT_FOUND;

@RequiredArgsConstructor
@Transactional
@Service
public class ScheduleCommandServiceImpl implements ScheduleCommandService{

    private final ScheduleRepository scheduleRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long createSchedule(ScheduleDto scheduleDto, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));

        List<String> scheduleMembers = scheduleDto.getScheduleMembers();
        List<ScheduleMember> scheduleMemberList = new ArrayList<>();
        for (String scheduleMember : scheduleMembers) {
            // TODO scheduleMember 저장
        }

        Schedule schedule = scheduleRepository.save(scheduleDto.toEntity(team, scheduleMemberList));

        for (String username : scheduleDto.getScheduleMembers()) {
            Optional<Member> findMember = memberRepository.findByUsername(username);
            if (findMember.isPresent()) {
                Member member = findMember.get();
                ScheduleMember scheduleMember = ScheduleMember.builder().schedule(schedule).member(member).build();
                scheduleMember.addScheduleMember(schedule, member);
            }
        }
        scheduleRepository.save(schedule);
        return schedule.getId();
    }

    @Override
    public Long updateSchedule(ScheduleDto scheduleDto) {
        Schedule schedule = scheduleRepository.findById(scheduleDto.getId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        List<ScheduleMember> scheduleMembers = scheduleRepository.findAllScheduleMembersByUsername(scheduleDto.getScheduleMembers());
        schedule.update(scheduleDto, scheduleMembers);
        Schedule updatedSchedule = scheduleRepository.findById(scheduleDto.getId())
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        return schedule.getId();
    }

    @Override
    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleHandler(ErrorStatus.SCHEDULE_NOT_FOUND));
        scheduleRepository.delete(schedule);
    }
}
