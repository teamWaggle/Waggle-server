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
import com.example.waggle.global.exception.handler.ScheduleHandler;
<<<<<<< HEAD
import com.example.waggle.web.dto.member.MemberRequest.MemberCredentialsDto;
import com.example.waggle.web.dto.schedule.ScheduleRequest.ScheduleCreateDto;
import com.example.waggle.web.dto.schedule.TeamRequest.TeamCreateDto;
=======
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.schedule.ScheduleRequest;
import com.example.waggle.web.dto.schedule.TeamRequest;
>>>>>>> develop
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class ScheduleCommandServiceTest {

    @Autowired
    ScheduleCommandService scheduleCommandService;
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
    @PersistenceContext
    EntityManager em;
    @Autowired
    DatabaseCleanUp databaseCleanUp;

<<<<<<< HEAD
    private MemberCredentialsDto member1;
    private MemberCredentialsDto member2;
    private TeamCreateDto team;
    private ScheduleCreateDto schedule;
=======
    private MemberRequest.AccessDto member1;
    private MemberRequest.AccessDto member2;
    private TeamRequest.Post team;
    private ScheduleRequest.Post schedule;
>>>>>>> develop

    private Long teamId;
    private Long scheduleId;
    private String username;


    @BeforeEach
    void setUp() {
        // Setup member
<<<<<<< HEAD
        member1 = MemberCredentialsDto.builder()
=======
        member1 = MemberRequest.AccessDto.builder()
>>>>>>> develop
                .email("member1")
                .password("12345678")
                .build();

<<<<<<< HEAD
        member2 = MemberCredentialsDto.builder()
=======
        member2 = MemberRequest.AccessDto.builder()
>>>>>>> develop
                .password("12345678")
                .email("email2")
                .build();
        Long memberId = memberCommandService.signUp(member1);
        memberCommandService.signUp(member2);
        Member member = memberQueryService.getMemberById(memberId);
        username = member.getUsername();

        // Setup team
        team = TeamCreateDto.builder()
                .name("team1")
                .description("team1 description")
                .maxTeamSize(4)
                .teamColor("team_3")
                .build();
        teamId = teamCommandService.createTeam(team, username);

        // Setup Schedule
        schedule = ScheduleCreateDto.builder()
                .title("schedule1")
                .content("schedule1 content")
                .startTime(LocalDateTime.of(2023, 12, 12, 9, 30))
                .endTime(LocalDateTime.of(2024, 1, 12, 9, 30))
                .build();
        scheduleId = scheduleCommandService.createSchedule(teamId, schedule, username);
    }

    @AfterEach
    void clean() {
        databaseCleanUp.truncateAllEntity();
    }


    @Test
    void createSchedule() {
        // given
        ScheduleCreateDto createRequest = ScheduleCreateDto.builder()
                .title("test title")
                .content("test content")
                .startTime(LocalDateTime.of(2023, 12, 12, 9, 30))
                .endTime(LocalDateTime.of(2023, 12, 13, 19, 30))
                .build();

        // when
        Long createdScheduleId = scheduleCommandService.createSchedule(teamId, createRequest, username);

        // then
        Schedule createdSchedule = scheduleQueryService.getScheduleById(createdScheduleId);
        assertThat(createdSchedule.getTitle()).isEqualTo(createRequest.getTitle());
        assertThat(createdSchedule.getContent()).isEqualTo(createRequest.getContent());
        assertThat(createdSchedule.getStartTime()).isEqualTo(createRequest.getStartTime());
        assertThat(createdSchedule.getEndTime()).isEqualTo(createRequest.getEndTime());
    }

    @Test
    void updateSchedule() {
        // given
        ScheduleCreateDto updateRequest = ScheduleCreateDto.builder()
                .title("updated title")
                .content("updated content")
                .startTime(LocalDateTime.of(2023, 12, 1, 9, 30))
                .endTime(LocalDateTime.of(2023, 12, 2, 19, 30))
                .build();

        // when
        Long updatedScheduleId = scheduleCommandService.updateScheduleByUsername(scheduleId, username, updateRequest);

        // then
        Schedule updatedSchedule = scheduleQueryService.getScheduleById(updatedScheduleId);
        assertThat(updatedSchedule.getTitle()).isEqualTo(updateRequest.getTitle());
        assertThat(updatedSchedule.getContent()).isEqualTo(updateRequest.getContent());
        assertThat(updatedSchedule.getStartTime()).isEqualTo(updateRequest.getStartTime());
        assertThat(updatedSchedule.getEndTime()).isEqualTo(updateRequest.getEndTime());
    }

    @Test
    void deleteSchedule() {
        // when
        scheduleCommandService.deleteScheduleByUsername(scheduleId, username);
        //then
        Assertions.assertThrows(ScheduleHandler.class, () -> scheduleQueryService.getScheduleById(scheduleId));
    }
}