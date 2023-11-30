package com.example.waggle.schedule.service;

import com.example.waggle.commons.component.DatabaseCleanUp;
import com.example.waggle.member.dto.MemberSummaryDto;
import com.example.waggle.member.dto.SignUpDto;
import com.example.waggle.member.repository.MemberRepository;
import com.example.waggle.schedule.dto.ScheduleDto;
import com.example.waggle.schedule.dto.TeamDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class ScheduleServiceTest {
    @Autowired
    DatabaseCleanUp databaseCleanUp;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    TeamService teamService;
    @Autowired
    ScheduleService scheduleService;

    private MemberSummaryDto memberSummaryDto1;
    private MemberSummaryDto memberSummaryDto2;
    private Long savedTeamId1;
    private Long savedTeamId2;
    private Long savedScheduleId1;
    private Long savedScheduleId2;

    @BeforeEach
    void beforeEach() {
        // member1, member2 생성
        SignUpDto signUpDto1 = SignUpDto.builder()
                .username("member1")
                .password("12345678")
                .build();
        SignUpDto signUpDto2 = SignUpDto.builder()
                .username("member2")
                .password("12345678")
                .build();

        memberSummaryDto1 = memberService.signUp(signUpDto1, null);
        memberSummaryDto2 = memberService.signUp(signUpDto2, null);

        // team 생성
        TeamDto team = TeamDto.builder()
                .name("team").build();
        savedTeamId1 = teamService.createTeam(team, memberSummaryDto1.getUsername());
        savedTeamId2 = teamService.addMember(savedTeamId1, memberSummaryDto2.getUsername());


        // schedule 생성
        ScheduleDto scheduleDto1 = ScheduleDto.builder()
                .title("산책")
//                .scheduleTime(LocalDateTime.now())
                .build();

        ScheduleDto scheduleDto2 = ScheduleDto.builder()
                .title("목욕")
//                .scheduleTime(LocalDateTime.now())
                .build();

        savedScheduleId1 = scheduleService.createSchedule(scheduleDto1, savedTeamId2);
        savedScheduleId2 = scheduleService.createSchedule(scheduleDto2, savedTeamId2);
    }

    @AfterEach
    void afterEach() {
        databaseCleanUp.truncateAllEntity();
    }

    @Test
    public void findByScheduleId() {
        ScheduleDto findScheduleDto = scheduleService.getScheduleById(savedScheduleId1);
        assertThat(findScheduleDto.getId()).isEqualTo(savedScheduleId1);
    }

    @Test
    public void findByTeamId() {
        List<ScheduleDto> result = scheduleService.getSchedulesByTeamId(savedTeamId2);
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void addSchedule() {
        ScheduleDto scheduleDto = ScheduleDto.builder()
                .title("병원")
                .scheduleTime(LocalDateTime.now())
                .build();

        scheduleDto.getScheduleMembers().add(memberSummaryDto1.getUsername());

        Long scheduleId = scheduleService.createSchedule(scheduleDto, savedTeamId2);
        ScheduleDto savedScheduleDto = scheduleService.getScheduleById(scheduleId);
        assertThat(savedScheduleDto.getScheduleMembers().size()).isEqualTo(1);
    }



    @Test
    public void updateSchedule() {
        ScheduleDto updateScheduleDto = ScheduleDto.builder()
                .id(savedScheduleId1)
                .title("한강 산책")
                .description("🐶🐶🐶")
                .scheduleTime(LocalDateTime.of(2023, 5, 26, 19, 30))
                .build();
        List<MemberSummaryDto> memberSummaryDtos = new ArrayList<>();
        memberSummaryDtos.add(memberSummaryDto1);
        memberSummaryDtos.add(memberSummaryDto2);

        // update
        Long scheduleId = scheduleService.updateSchedule(updateScheduleDto);
        ScheduleDto updatedScheduleDto = scheduleService.getScheduleById(scheduleId);

        assertThat(updatedScheduleDto.getTitle()).isEqualTo(updateScheduleDto.getTitle());
    }

    @Test
    public void removeSchedule() {
        scheduleService.deleteSchedule(savedScheduleId1);
        List<ScheduleDto> result = scheduleService.getSchedulesByTeamId(savedTeamId2);
        assertThat(result.size()).isEqualTo(1);
    }

}