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
        return result.stream().map(ScheduleDto::toDto).collect(Collectors.toList());
    }


    @Transactional
    public ScheduleDto addSchedule(ScheduleDto scheduleDto, Long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);

        if (team.isPresent()) {
            Schedule schedule = scheduleRepository.save(scheduleDto.toEntity(team.get()));

            for (String username : scheduleDto.getScheduleMembers()) {
                Optional<Member> findMember = memberRepository.findByUsername(username);
                if (findMember.isPresent()) {
                    Member member = findMember.get();
                    ScheduleMember scheduleMember = ScheduleMember.builder()
                            .schedule(schedule)
                            .member(member).build();
                    scheduleMember.addScheduleMember(schedule, member); // 연관관계 메서드
                }
            }
            scheduleRepository.save(schedule);
            return ScheduleDto.toDto(schedule);

        } else {
            // TODO 예외 처리
            return null;
        }
    }

    @Transactional
    public ScheduleDto updateSchedule(Long scheduleId, ScheduleDto scheduleDto) {
        Optional<Schedule> scheduleToUpdate = scheduleRepository.findById(scheduleId);
        if (scheduleToUpdate.isPresent()) {
            Schedule schedule = scheduleToUpdate.get();
            List<ScheduleMember> scheduleMembers = scheduleRepository.findAllScheduleMembersByUsername(scheduleDto.getScheduleMembers());
            schedule.update(scheduleDto, scheduleMembers);  // dirty-checking
            Schedule updatedSchedule = scheduleRepository.findById(scheduleId).get();
            return ScheduleDto.toDto(updatedSchedule);
        } else {
            // TODO 예외 처리
            return null;
        }
    }

    @Transactional
    public Boolean removeSchedule(Long scheduleId) {
        Optional<Schedule> scheduleToRemove = scheduleRepository.findById(scheduleId);
        if (scheduleToRemove.isPresent()) {
            Schedule schedule = scheduleToRemove.get();
            scheduleRepository.delete(schedule);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
