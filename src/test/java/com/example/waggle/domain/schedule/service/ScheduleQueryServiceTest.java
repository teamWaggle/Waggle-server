package com.example.waggle.domain.schedule.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.service.schedule.ScheduleCommandService;
import com.example.waggle.domain.schedule.service.schedule.ScheduleQueryService;
import com.example.waggle.domain.schedule.service.team.TeamCommandService;
import com.example.waggle.domain.schedule.service.team.TeamQueryService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.member.MemberRequest.TemporaryRegisterDto;
import com.example.waggle.web.dto.schedule.ScheduleRequest.ScheduleCreateDto;
import com.example.waggle.web.dto.schedule.TeamRequest.TeamCreateDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ScheduleQueryServiceTest {

    @Autowired
    ScheduleQueryService scheduleQueryService;
    @Autowired
    MemberCommandService memberCommandService;
    @Autowired
    MemberQueryService memberQueryService;
    @Autowired
    TeamCommandService teamCommandService;
    @Autowired
    TeamQueryService teamQueryService;
    @Autowired
    ScheduleCommandService scheduleCommandService;
    @Autowired
    DatabaseCleanUp databaseCleanUp;

    private Member member1;
    private Member member2;
    private Long teamId;
    private Long scheduleId;

    @BeforeEach
    void setUp() {
        // Setup member
        TemporaryRegisterDto A = TemporaryRegisterDto.builder()
                .password("password")
                .email("email")
                .build();
        TemporaryRegisterDto B = TemporaryRegisterDto.builder()
                .password("password")
                .email("email1")
                .build();
        Long memberA = memberCommandService.signUp(A);
        Long memberB = memberCommandService.signUp(B);
        member1 = memberQueryService.getMemberById(memberA);
        member2 = memberQueryService.getMemberById(memberB);
        TeamCreateDto build = TeamCreateDto.builder()
                .teamColor("team_3")
                .maxTeamSize(5)
                .name("team")
                .description("hello")
                .build();
        teamId = teamCommandService.createTeam(build, member1.getUsername());
        ScheduleCreateDto schedule = ScheduleCreateDto.builder()
                .title("schedule1")
                .content("hi")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .build();
        scheduleId = scheduleCommandService.createSchedule(teamId, schedule, member1.getUsername());

    }

    @AfterEach
    void clean() {
        databaseCleanUp.truncateAllEntity();
    }

    @Test
    void getScheduleById() {
        // when
        Schedule scheduleById = scheduleQueryService.getScheduleById(scheduleId);

        // then
        assertThat(scheduleById.getContent()).isEqualTo("hi");
    }

    @Test
    void getSchedulesByTeamId() {
        // given
        ScheduleCreateDto schedule2 = ScheduleCreateDto.builder()
                .title("schedule1")
                .content("hi")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .build();
        Long schedule = scheduleCommandService.createSchedule(teamId, schedule2, member1.getUsername());

        // when
        List<Schedule> schedules = scheduleQueryService.getTeamSchedules(teamId);

        // then
        assertThat(schedules.size()).isEqualTo(2);
    }

    @Test
    void getSchedulesByMember() {
        // given
        ScheduleCreateDto schedule2 = ScheduleCreateDto.builder()
                .title("schedule1")
                .content("hi")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .build();
        Long schedule = scheduleCommandService.createSchedule(teamId, schedule2, member1.getUsername());

        // when
        List<Schedule> schedules = scheduleQueryService.getSchedulesByMember(member1.getId());

        // then
        assertThat(schedules.size()).isEqualTo(2);
    }

    @Test
    void getMonthlySchedulesByMember() {
        // given

        // Setup team1 schedules
        for (int i = 2; i <= 10; i++) {
            ScheduleCreateDto newSchedule = ScheduleCreateDto.builder()
                    .title("team1 schedule" + i)
                    .content("team1 schedule" + i + " content")
                    .startTime(LocalDateTime.of(2023, i, 12, 9, 30))
                    .endTime(LocalDateTime.of(2023, i + 1, 12, 9, 30))
                    .build();
            scheduleCommandService.createSchedule(teamId, newSchedule, member1.getUsername());
        }

        // when
        List<Schedule> monthlySchedules = scheduleQueryService.getMonthlySchedulesByMember(
                member1.getId(), 2023, 10);

        // then
        assertThat(monthlySchedules.size()).isEqualTo(2);
    }
}