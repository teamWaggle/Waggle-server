package com.example.waggle.domain.schedule.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.schedule.entity.Schedule;
import com.example.waggle.domain.schedule.entity.Team;
import com.example.waggle.domain.schedule.entity.TeamMember;
import com.example.waggle.domain.schedule.repository.ScheduleRepository;
import com.example.waggle.domain.schedule.repository.TeamMemberRepository;
import com.example.waggle.domain.schedule.repository.TeamRepository;
import com.example.waggle.domain.schedule.service.schedule.ScheduleQueryService;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@WithMockCustomUser
@Transactional
@SpringBootTest
class ScheduleQueryServiceTest {

    @Autowired
    ScheduleQueryService scheduleQueryService;
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
    private Team team1;
    private Team team2;
    private Schedule schedule1;

    @BeforeEach
    void setUp() {
        // Setup member
        member1 = Member.builder()
                .username("member1")
                .password("12345678")
                .nickname("sdfjsakld")
                .email("dalsfjk")
                .build();
        memberRepository.save(member1);

        member2 = Member.builder()
                .username("member2")
                .password("12345678")
                .nickname("alsdkfj")
                .email("23fdfx")
                .build();
        memberRepository.save(member2);

        // Setup team
        team1 = Team.builder()
                .name("team1")
                .description("team1 description")
                .leader(member1)
                .maxTeamSize(4)
                .colorScheme("red")
                .build();
        teamRepository.save(team1);

        team2 = Team.builder()
                .name("team2")
                .description("team2 description")
                .leader(member1)
                .maxTeamSize(4)
                .colorScheme("orange")
                .build();
        teamRepository.save(team2);

        // Setup teamMember
        addMemberToTeam(team1, member1);
        addMemberToTeam(team2, member1);

        // Setup Schedule
        schedule1 = Schedule.builder()
                .title("schedule1")
                .content("schedule1 content")
                .startTime(LocalDateTime.of(2023, 12, 12, 9, 30))
                .endTime(LocalDateTime.of(2024, 1, 12, 9, 30))
                .member(member1)
                .build();
        scheduleRepository.save(schedule1);
        team1.addSchedule(schedule1);
    }

    private void addMemberToTeam(Team team, Member member) {
        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .member(member)
                .build();
        teamMember.addTeamMember(team, member);
        teamMemberRepository.save(teamMember);
    }

    @Test
    void getScheduleById() {
        // when
        Schedule scheduleById = scheduleQueryService.getScheduleById(schedule1.getId());

        // then
        assertThat(scheduleById).isEqualTo(schedule1);
    }

    @Test
    void getSchedulesByTeamId() {
        // given
        Schedule schedule2 = Schedule.builder()
                .title("schedule2")
                .content("schedule2 content")
                .startTime(LocalDateTime.of(2023, 12, 12, 9, 30))
                .endTime(LocalDateTime.of(2024, 1, 12, 9, 30))
                .member(member1)
                .build();
        scheduleRepository.save(schedule2);
        team1.addSchedule(schedule2);

        // when
        List<Schedule> schedules = scheduleQueryService.getTeamSchedules(team1.getId());

        // then
        assertThat(schedules.size()).isEqualTo(2);
        assertThat(schedules).contains(schedule1, schedule2);
    }

    @Test
    void getSchedulesByMemberUsername() {
        // given
        Schedule schedule2 = Schedule.builder()
                .title("schedule2")
                .content("schedule2 content")
                .startTime(LocalDateTime.of(2023, 12, 12, 9, 30))
                .endTime(LocalDateTime.of(2024, 1, 12, 9, 30))
                .member(member1)
                .build();
        scheduleRepository.save(schedule2);
        team2.addSchedule(schedule2);

        // when
        List<Schedule> schedules = scheduleQueryService.getSchedulesByMember(member1.getId());

        // then
        assertThat(schedules.size()).isEqualTo(2);
        assertThat(schedules).contains(schedule1, schedule2);
    }

    @Test
    void getMonthlySchedulesByMember() {
        // given

        // Setup team1 schedules
        for (int i = 2; i <= 10; i++) {
            Schedule newSchedule = Schedule.builder()
                    .title("team1 schedule" + i)
                    .content("team1 schedule" + i + " content")
                    .startTime(LocalDateTime.of(2023, i, 12, 9, 30))
                    .endTime(LocalDateTime.of(2023, i + 1, 12, 9, 30))
                    .member(member1)
                    .build();
            scheduleRepository.save(newSchedule);
            team1.addSchedule(newSchedule);
        }

        // Setup team2 schedules
        for (int i = 1; i <= 10; i++) {
            Schedule newSchedule = Schedule.builder()
                    .title("team2 schedule" + i)
                    .content("team2 schedule" + i + " content")
                    .startTime(LocalDateTime.of(2023, i, 12, 9, 30))
                    .endTime(LocalDateTime.of(2023, i + 1, 12, 9, 30))
                    .member(member1)
                    .build();
            scheduleRepository.save(newSchedule);
            team2.addSchedule(newSchedule);
        }

        // when
        List<Schedule> monthlySchedules = scheduleQueryService.getMonthlySchedulesByMember(
                member1.getId(), 2023, 10);

        // then
        assertThat(monthlySchedules.size()).isEqualTo(4);
        assertThat(monthlySchedules.get(0).getTitle()).isEqualTo("team1 schedule9");
        assertThat(monthlySchedules.get(1).getTitle()).isEqualTo("team1 schedule10");
        assertThat(monthlySchedules.get(2).getTitle()).isEqualTo("team2 schedule9");
        assertThat(monthlySchedules.get(3).getTitle()).isEqualTo("team2 schedule10");
    }
}