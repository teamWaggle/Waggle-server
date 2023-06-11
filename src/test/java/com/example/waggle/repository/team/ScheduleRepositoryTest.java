package com.example.waggle.repository.team;

import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.team.Schedule;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.ScheduleDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.dto.member.TeamDto;
import com.example.waggle.repository.member.MemberRepository;
import com.example.waggle.service.member.MemberService;
import com.example.waggle.service.team.ScheduleService;
import com.example.waggle.service.team.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ScheduleRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    TeamService teamService;
    @Autowired
    ScheduleService scheduleService;

    @Test
    @Transactional
    void findAllByTeamId() {
        // given
        // Member 회원가입
        SignUpDto signUpDto = SignUpDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();

        MemberDto savedMemberDto = memberService.signUp(signUpDto);

        // team
        TeamDto team = TeamDto.builder()
                .name("team").build();
        TeamDto savedTeamDto = teamService.createTeamWithMember(team, savedMemberDto);

        ScheduleDto scheduleDto1 = ScheduleDto.builder()
                .title("산책")
                .scheduleTime(LocalDateTime.now())
                .build();

        ScheduleDto scheduleDto2 = ScheduleDto.builder()
                .title("목욕")
                .scheduleTime(LocalDateTime.now())
                .build();

        ScheduleDto scheduleDto3 = ScheduleDto.builder()
                .title("애견카페")
                .scheduleTime(LocalDateTime.now())
                .build();


        ScheduleDto savedScheduleDto1 = scheduleService.addSchedule(scheduleDto1, savedTeamDto.getId());
        ScheduleDto savedScheduleDto2 = scheduleService.addSchedule(scheduleDto2, savedTeamDto.getId());
        ScheduleDto savedScheduleDto3 = scheduleService.addSchedule(scheduleDto3, savedTeamDto.getId());


        // when
        List<ScheduleDto> result = scheduleService.findByTeamId(savedTeamDto.getId());

        // then
        assertThat(result.size()).isEqualTo(3);
        assertThat(result).usingRecursiveFieldByFieldElementComparator().contains(savedScheduleDto1, savedScheduleDto2, savedScheduleDto3);

    }
}