package com.example.waggle.domain.schedule.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.schedule.domain.Schedule;
import com.example.waggle.domain.schedule.domain.Team;
import com.example.waggle.domain.schedule.domain.TeamMember;
import com.example.waggle.domain.schedule.repository.ScheduleRepository;
import com.example.waggle.domain.schedule.repository.TeamMemberRepository;
import com.example.waggle.domain.schedule.repository.TeamRepository;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.web.dto.schedule.ScheduleRequest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@WithMockCustomUser
@Transactional
@SpringBootTest
class ScheduleCommandServiceTest {

    @Autowired
    DatabaseCleanUp databaseCleanUp;
    @Autowired
    ScheduleCommandService scheduleCommandService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    TeamMemberRepository teamMemberRepository;
    @Autowired
    ScheduleRepository scheduleRepository;

    private Member member1;
    private Member member2;
    private Team team;
    private Schedule schedule;


    @BeforeEach
    void setUp() {
        // Setup member
        member1 = Member.builder()
                .username("member1")
                .password("12345678")
                .build();
        memberRepository.save(member1);

        member2 = Member.builder()
                .username("member2")
                .password("12345678")
                .build();
        memberRepository.save(member2);

        // Setup team
        team = Team.builder()
                .name("team1")
                .description("team1 description")
                .leader(member1)
                .maxTeamSize(4)
                .build();
        teamRepository.save(team);

        // Setup teamMember
        TeamMember teamMember = TeamMember.builder()
                .member(member1)
                .team(team)
                .build();
        teamMember.addTeamMember(team, member1);

        // Setup Schedule
        schedule = Schedule.builder()
                .title("schedule1")
                .content("schedule1 content")
                .startTime(LocalDateTime.of(2023, 12, 12, 9, 30))
                .endTime(LocalDateTime.of(2024, 1, 12, 9, 30))
                .team(team)
                .build();
        scheduleRepository.save(schedule);
    }

    @AfterEach
    void clean() {
        databaseCleanUp.truncateAllEntity();
    }


    @Test
    void createSchedule() {
        // given
        ScheduleRequest.Post createRequest = ScheduleRequest.Post.builder()
                .title("test title")
                .content("test content")
                .startTime(LocalDateTime.of(2023, 12, 12, 9, 30))
                .endTime(LocalDateTime.of(2023, 12, 13, 19, 30))
                .build();

        // when
        Long createdScheduleId = scheduleCommandService.createSchedule(team.getId(), createRequest);

        // then
        Schedule createdSchedule = scheduleRepository.findById(createdScheduleId).get();
        assertThat(createdSchedule.getTitle()).isEqualTo(createRequest.getTitle());
        assertThat(createdSchedule.getContent()).isEqualTo(createRequest.getContent());
        assertThat(createdSchedule.getStartTime()).isEqualTo(createRequest.getStartTime());
        assertThat(createdSchedule.getEndTime()).isEqualTo(createRequest.getEndTime());
        assertThat(createdSchedule.getTeam().getName()).isEqualTo(team.getName());
        assertThat(team.getSchedules().size()).isEqualTo(1);
        assertThat(team.getSchedules().get(0).getTitle()).isEqualTo(createRequest.getTitle());
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
        Long updatedScheduleId = scheduleCommandService.updateSchedule(schedule.getId(), updateRequest);

        // then
        Schedule updatedSchedule = scheduleRepository.findById(updatedScheduleId).get();
        assertThat(updatedSchedule.getTitle()).isEqualTo(updateRequest.getTitle());
        assertThat(updatedSchedule.getContent()).isEqualTo(updateRequest.getContent());
        assertThat(updatedSchedule.getStartTime()).isEqualTo(updateRequest.getStartTime());
        assertThat(updatedSchedule.getEndTime()).isEqualTo(updateRequest.getEndTime());
    }

    @Test
    void deleteSchedule() {
        // when
        scheduleCommandService.deleteSchedule(schedule.getId());

        // then
        assertThat(scheduleRepository.existsById(schedule.getId())).isEqualTo(false);
    }
}