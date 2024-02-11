package com.example.waggle.domain.schedule.service;

import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.entity.Team;
import com.example.waggle.domain.schedule.service.schedule.ScheduleCommandService;
import com.example.waggle.domain.schedule.service.schedule.ScheduleQueryService;
import com.example.waggle.domain.schedule.service.team.TeamCommandService;
import com.example.waggle.domain.schedule.service.team.TeamQueryService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.global.exception.handler.ScheduleHandler;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.schedule.ScheduleRequest;
import com.example.waggle.web.dto.schedule.TeamRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
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
@WithMockCustomUser
@SpringBootTest
class ScheduleCommandServiceTest {

    @Autowired
    ScheduleCommandService scheduleCommandService;
    @Autowired
    ScheduleQueryService scheduleQueryService;
    @Autowired
    MemberCommandService memberCommandService;
    @Autowired
    TeamCommandService teamCommandService;
    @Autowired
    TeamQueryService teamQueryService;
    @PersistenceContext
    EntityManager em;
    @Autowired
    DatabaseCleanUp databaseCleanUp;

    private MemberRequest.RegisterDto member1;
    private MemberRequest.RegisterDto member2;
    private TeamRequest.Post team;
    private ScheduleRequest.Post schedule;

    private Long teamId;
    private Long scheduleId;


    @BeforeEach
    void setUp() {
        // Setup member
        member1 = MemberRequest.RegisterDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("nickname1")
                .email("email1")
                .build();

        member2 = MemberRequest.RegisterDto.builder()
                .username("member2")
                .password("12345678")
                .nickname("nickname2")
                .email("email2")
                .build();
        memberCommandService.signUp(member1);
        memberCommandService.signUp(member2);

        // Setup team
        team = TeamRequest.Post.builder()
                .name("team1")
                .description("team1 description")
                .maxTeamSize(4)
                .colorScheme("red")
                .build();
        teamId = teamCommandService.createTeam(team, "member1");

        // Setup Schedule
        schedule = ScheduleRequest.Post.builder()
                .title("schedule1")
                .content("schedule1 content")
                .startTime(LocalDateTime.of(2023, 12, 12, 9, 30))
                .endTime(LocalDateTime.of(2024, 1, 12, 9, 30))
                .build();
        scheduleId = scheduleCommandService.createSchedule(teamId, schedule, "member1");
    }

    @AfterEach
    void clean() {
        databaseCleanUp.truncateAllEntity();
    }


    @Test
    @Transactional
    void createSchedule() {
        // given
        ScheduleRequest.Post createRequest = ScheduleRequest.Post.builder()
                .title("test title")
                .content("test content")
                .startTime(LocalDateTime.of(2023, 12, 12, 9, 30))
                .endTime(LocalDateTime.of(2023, 12, 13, 19, 30))
                .build();

        // when
        Long createdScheduleId = scheduleCommandService.createSchedule(teamId, createRequest, "member1");

        // then
        Schedule createdSchedule = scheduleQueryService.getScheduleById(createdScheduleId);
        Team teamById = teamQueryService.getTeamById(teamId);
        assertThat(createdSchedule.getTitle()).isEqualTo(createRequest.getTitle());
        assertThat(createdSchedule.getContent()).isEqualTo(createRequest.getContent());
        assertThat(createdSchedule.getStartTime()).isEqualTo(createRequest.getStartTime());
        assertThat(createdSchedule.getEndTime()).isEqualTo(createRequest.getEndTime());
        assertThat(createdSchedule.getTeam().getName()).isEqualTo(team.getName());
        assertThat(teamById.getSchedules().size()).isEqualTo(2);
        assertThat(teamById.getSchedules().get(1).getTitle()).isEqualTo(createRequest.getTitle());
    }

    @Test
    void updateSchedule() {
        // given
        ScheduleRequest.Post updateRequest = ScheduleRequest.Post.builder()
                .title("updated title")
                .content("updated content")
                .startTime(LocalDateTime.of(2023, 12, 1, 9, 30))
                .endTime(LocalDateTime.of(2023, 12, 2, 19, 30))
                .build();

        // when
        Long updatedScheduleId = scheduleCommandService.updateSchedule(scheduleId, updateRequest);

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
        scheduleCommandService.deleteSchedule(scheduleId);
        //then
        Assertions.assertThrows(ScheduleHandler.class, () -> scheduleQueryService.getScheduleById(scheduleId));
    }
}