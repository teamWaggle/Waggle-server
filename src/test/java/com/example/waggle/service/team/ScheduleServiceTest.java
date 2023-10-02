package com.example.waggle.service.team;

import com.example.waggle.commons.component.DatabaseCleanUp;
import com.example.waggle.member.dto.MemberDetailDto;
import com.example.waggle.member.dto.SignUpDto;
import com.example.waggle.schedule.dto.ScheduleDto;
import com.example.waggle.schedule.service.ScheduleService;
import com.example.waggle.schedule.dto.TeamDto;
import com.example.waggle.member.repository.MemberRepository;
import com.example.waggle.member.service.MemberService;
import com.example.waggle.schedule.service.TeamService;
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

    private MemberDetailDto savedMemberDetailDto1;
    private MemberDetailDto savedMemberDetailDto2;
    private TeamDto savedTeamDto1;
    private TeamDto savedTeamDto2;
    private ScheduleDto savedScheduleDto1;
    private ScheduleDto savedScheduleDto2;

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

        savedMemberDetailDto1 = memberService.signUp(signUpDto1, null);
        savedMemberDetailDto2 = memberService.signUp(signUpDto2, null);

        // team 생성
        TeamDto team = TeamDto.builder()
                .name("team").build();
        savedTeamDto1 = teamService.createTeam(team, savedMemberDetailDto1.getUsername());
        savedTeamDto2 = teamService.addMember(savedTeamDto1.getId(), savedMemberDetailDto2.getUsername());


        // schedule 생성
        ScheduleDto scheduleDto1 = ScheduleDto.builder()
                .title("산책")
//                .scheduleTime(LocalDateTime.now())
                .build();

        ScheduleDto scheduleDto2 = ScheduleDto.builder()
                .title("목욕")
//                .scheduleTime(LocalDateTime.now())
                .build();

        savedScheduleDto1 = scheduleService.createSchedule(scheduleDto1, savedTeamDto2.getId());
        savedScheduleDto2 = scheduleService.createSchedule(scheduleDto2, savedTeamDto2.getId());
    }

    @AfterEach
    void afterEach() {
        databaseCleanUp.truncateAllEntity();
    }

    @Test
    public void findByScheduleId() {
        ScheduleDto findScheduleDto = scheduleService.getScheduleById(savedScheduleDto1.getId());
        assertThat(findScheduleDto).usingRecursiveComparison().isEqualTo(savedScheduleDto1);
    }

    @Test
    public void findByTeamId() {
        List<ScheduleDto> result = scheduleService.getSchedulesByTeamId(savedTeamDto2.getId());
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).usingRecursiveFieldByFieldElementComparator().contains(savedScheduleDto1, savedScheduleDto2);
    }

    @Test
    public void addSchedule() {
        ScheduleDto scheduleDto = ScheduleDto.builder()
                .title("병원")
                .scheduleTime(LocalDateTime.now())
                .build();

        scheduleDto.getScheduleMembers().add(savedMemberDetailDto1.getUsername());

        ScheduleDto savedScheduleDto = scheduleService.createSchedule(scheduleDto, savedTeamDto2.getId());
        assertThat(savedScheduleDto.getScheduleMembers().size()).isEqualTo(1);
    }



    @Test
    public void updateSchedule() {
        ScheduleDto updateScheduleDto = ScheduleDto.builder()
                .id(savedScheduleDto1.getId())
                .title("한강 산책")
                .description("🐶🐶🐶")
                .scheduleTime(LocalDateTime.of(2023, 5, 26, 19, 30))
                .build();
        List<MemberDetailDto> memberDetailDtos = new ArrayList<>();
        memberDetailDtos.add(savedMemberDetailDto1);
        memberDetailDtos.add(savedMemberDetailDto2);

        // update
        ScheduleDto updatedScheduleDto = scheduleService.updateSchedule(updateScheduleDto);

        assertThat(updatedScheduleDto.getTitle()).isEqualTo(updateScheduleDto.getTitle());
    }

    @Test
    public void removeSchedule() {
        scheduleService.deleteSchedule(savedScheduleDto1.getId());
        List<ScheduleDto> result = scheduleService.getSchedulesByTeamId(savedTeamDto2.getId());
        assertThat(result.size()).isEqualTo(1);
        assertThat(result).usingRecursiveFieldByFieldElementComparator().doesNotContain(savedScheduleDto1);
    }

    @Test
//    @Transactional(readOnly = true)
    public void test() {
        log.info("@Transactional(readOnly = true)");
    }
}