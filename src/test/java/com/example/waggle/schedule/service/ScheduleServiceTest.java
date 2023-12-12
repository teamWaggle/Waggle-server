//package com.example.waggle.schedule.service;
//
//import com.example.waggle.domain.member.repository.MemberRepository;
//import com.example.waggle.domain.member.service.MemberCommandService;
//import com.example.waggle.domain.schedule.service.ScheduleCommandService;
//import com.example.waggle.domain.schedule.service.ScheduleQueryService;
//import com.example.waggle.domain.schedule.service.TeamCommandService;
//import com.example.waggle.domain.schedule.service.TeamQueryService;
//import com.example.waggle.global.component.DatabaseCleanUp;
//import com.example.waggle.web.dto.member.MemberRequest;
//import com.example.waggle.web.dto.member.MemberResponse;
//import com.example.waggle.web.dto.schedule.ScheduleDto;
//import com.example.waggle.web.dto.schedule.TeamDto;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Slf4j
//class ScheduleServiceTest {
//    @Autowired
//    DatabaseCleanUp databaseCleanUp;
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired
//    MemberCommandService memberService;
//    @Autowired
//    TeamCommandService teamService;
//    @Autowired
//    TeamQueryService teamQueryService;
//    @Autowired
//    ScheduleCommandService scheduleService;
//    @Autowired
//    ScheduleQueryService scheduleQueryService;
//
//    private MemberResponse.MemberSummaryDto memberSummaryDto1;
//    private MemberResponse.MemberSummaryDto memberSummaryDto2;
//    private Long savedTeamId1;
//    private Long savedTeamId2;
//    private Long savedScheduleId1;
//    private Long savedScheduleId2;
//
//    @BeforeEach
//    void beforeEach() {
//        // member1, member2 생성
//        MemberRequest.RegisterRequestDto signUpDto1 = MemberRequest.RegisterRequestDto.builder()
//                .username("member1")
//                .password("12345678")
//                .build();
//        MemberRequest.RegisterRequestDto signUpDto2 = MemberRequest.RegisterRequestDto.builder()
//                .username("member2")
//                .password("12345678")
//                .build();
//
//        memberService.signUp(signUpDto1);
//        memberService.signUp(signUpDto2);
//
//        // team 생성
//        TeamDto team = TeamDto.builder()
//                .name("team").build();
//        savedTeamId1 = teamService.createTeam(team, memberSummaryDto1.getUsername());
//        savedTeamId2 = teamService.addTeamMember(savedTeamId1, memberSummaryDto2.getUsername());
//
//
//        // schedule 생성
//        ScheduleDto scheduleDto1 = ScheduleDto.builder()
//                .title("산책")
////                .scheduleTime(LocalDateTime.now())
//                .build();
//
//        ScheduleDto scheduleDto2 = ScheduleDto.builder()
//                .title("목욕")
////                .scheduleTime(LocalDateTime.now())
//                .build();
//
//        savedScheduleId1 = scheduleService.createSchedule(scheduleDto1, savedTeamId2);
//        savedScheduleId2 = scheduleService.createSchedule(scheduleDto2, savedTeamId2);
//    }
//
//    @AfterEach
//    void afterEach() {
//        databaseCleanUp.truncateAllEntity();
//    }
//
//    @Test
//    public void findByScheduleId() {
//        ScheduleDto findScheduleDto = scheduleQueryService.getScheduleById(savedScheduleId1);
//        assertThat(findScheduleDto.getId()).isEqualTo(savedScheduleId1);
//    }
//
//    @Test
//    public void findByTeamId() {
//        List<ScheduleDto> result = scheduleQueryService.getSchedulesByTeamId(savedTeamId2);
//        assertThat(result.size()).isEqualTo(2);
//    }
//
//    @Test
//    public void addSchedule() {
//        ScheduleDto scheduleDto = ScheduleDto.builder()
//                .title("병원")
//                .scheduleTime(LocalDateTime.now())
//                .build();
//
//        scheduleDto.getScheduleMembers().add(memberSummaryDto1.getUsername());
//
//        Long scheduleId = scheduleService.createSchedule(scheduleDto, savedTeamId2);
//        ScheduleDto savedScheduleDto = scheduleQueryService.getScheduleById(scheduleId);
//        assertThat(savedScheduleDto.getScheduleMembers().size()).isEqualTo(1);
//    }
//
//
//
//    @Test
//    public void updateSchedule() {
//        ScheduleDto updateScheduleDto = ScheduleDto.builder()
//                .id(savedScheduleId1)
//                .title("한강 산책")
//                .description("🐶🐶🐶")
//                .scheduleTime(LocalDateTime.of(2023, 5, 26, 19, 30))
//                .build();
//        List<MemberResponse.MemberSummaryDto> memberSummaryDtos = new ArrayList<>();
//        memberSummaryDtos.add(memberSummaryDto1);
//        memberSummaryDtos.add(memberSummaryDto2);
//
//        // update
//        Long scheduleId = scheduleService.updateSchedule(updateScheduleDto);
//        ScheduleDto updatedScheduleDto = scheduleQueryService.getScheduleById(scheduleId);
//
//        assertThat(updatedScheduleDto.getTitle()).isEqualTo(updateScheduleDto.getTitle());
//    }
//
//    @Test
//    public void removeSchedule() {
//        scheduleService.deleteSchedule(savedScheduleId1);
//        List<ScheduleDto> result = scheduleQueryService.getSchedulesByTeamId(savedTeamId2);
//        assertThat(result.size()).isEqualTo(1);
//    }
//
//}