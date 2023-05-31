package com.example.waggle.service.team;

import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.team.Schedule;
import com.example.waggle.domain.team.ScheduleMember;
import com.example.waggle.domain.team.Team;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.ScheduleDto;
import com.example.waggle.repository.member.MemberRepository;
import com.example.waggle.repository.team.ScheduleRepository;
import com.example.waggle.repository.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    public Optional<ScheduleDto> findByScheduleId(Long scheduleId) {
        Optional<Schedule> findSchedule = scheduleRepository.findById(scheduleId);
        return findSchedule.map(ScheduleDto::toDto);
    }

    public List<ScheduleDto> findByTeamId(Long teamId) {
        List<Schedule> result = scheduleRepository.findAllByTeamId(teamId);
        return result.stream()
                .map(ScheduleDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ScheduleDto addSchedule(ScheduleDto scheduleDto, Long teamId, List<MemberDto> memberDtos) {
        Optional<Team> team = teamRepository.findById(teamId);

        List<ScheduleMember> scheduleMembers = scheduleRepository.findAllScheduleMembersByUsername(scheduleDto.getScheduleMembers());
        Schedule schedule = scheduleRepository.save(scheduleDto.toEntity(team.get(), scheduleMembers));

        for (MemberDto memberDto : memberDtos) {
            ScheduleMember scheduleMember = new ScheduleMember();
            scheduleMember.setScheduleMember(schedule, memberDto.toEntity());
        }

        return ScheduleDto.toDto(schedule);
    }

    @Transactional
    public ScheduleDto updateSchedule(Long scheduleId, ScheduleDto scheduleDto) {
        Optional<Schedule> findSchedule = scheduleRepository.findById(scheduleId);
        if (findSchedule.isPresent()) {
            Schedule schedule = findSchedule.get();
            List<ScheduleMember> scheduleMembers = scheduleRepository.findAllScheduleMembersByUsername(scheduleDto.getScheduleMembers());


            Schedule updatedSchedule = Schedule.builder()
                    .id(schedule.getId())
                    .team(schedule.getTeam())
                    .title(scheduleDto.getTitle())
                    .description(scheduleDto.getDescription())
                    .scheduleTime(scheduleDto.getScheduleTime())
                    .scheduleMembers(scheduleMembers)
                    .build();

            // 업데이트 된 스케쥴 저장
            Schedule savedSchedule = scheduleRepository.save(updatedSchedule);
            return ScheduleDto.toDto(savedSchedule);
        } else {
            // 존재하지 않는 스케쥴 처리
            return null;
        }
    }

    @Transactional
    public Boolean removeSchedule(Long scheduleId) {
        Optional<Schedule> removalSchedule = scheduleRepository.findById(scheduleId);
        if(removalSchedule.isPresent()) {
            Schedule schedule = removalSchedule.get();
            scheduleRepository.delete(schedule);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
