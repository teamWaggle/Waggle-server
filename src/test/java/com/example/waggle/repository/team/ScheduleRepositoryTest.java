package com.example.waggle.repository.team;

import com.example.waggle.member.dto.MemberDto;
import com.example.waggle.member.dto.SignUpDto;
import com.example.waggle.schedule.dto.ScheduleDto;
import com.example.waggle.schedule.dto.TeamDto;
import com.example.waggle.member.repository.MemberRepository;
import com.example.waggle.member.service.MemberService;
import com.example.waggle.schedule.service.ScheduleService;
import com.example.waggle.schedule.service.TeamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
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
        TeamDto savedTeamDto = teamService.createTeamWithMember(team, savedMemberDto.getUsername());

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