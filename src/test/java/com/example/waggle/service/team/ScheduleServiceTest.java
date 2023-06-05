package com.example.waggle.service.team;

import com.example.waggle.component.DatabaseCleanUp;
import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.team.ScheduleMember;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.ScheduleDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.dto.member.TeamDto;
import com.example.waggle.repository.member.MemberRepository;
import com.example.waggle.service.member.MemberService;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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

    private MemberDto savedMemberDto1;
    private MemberDto savedMemberDto2;
    private TeamDto savedTeamDto1;
    private TeamDto savedTeamDto2;
    private ScheduleDto savedScheduleDto1;
    private ScheduleDto savedScheduleDto2;
    private ScheduleDto savedScheduleDto3;

    @BeforeEach
    void beforeEach() {
        // Member 회원가입
        SignUpDto signUpDto1 = SignUpDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        SignUpDto signUpDto2 = SignUpDto.builder()
                .username("member2")
                .password("12345678")
                .nickname("닉네임2")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();

        savedMemberDto1 = memberService.signUp(signUpDto1);
        savedMemberDto2 = memberService.signUp(signUpDto2);

        // teamA
        TeamDto teamA = TeamDto.builder()
                .name("teamA").build();
        savedTeamDto1 = teamService.createTeamWithMember(teamA, savedMemberDto1);
        savedTeamDto2 = teamService.addMember(savedTeamDto1.getId(), savedMemberDto2.getUsername());


        // teamB
        TeamDto teamB = TeamDto.builder()
                .name("teamB").build();
        TeamDto savedTeamB = teamService.createTeamWithMember(teamB, savedMemberDto1);


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


        savedScheduleDto1 = scheduleService.addSchedule(scheduleDto1, savedTeamDto2.getId());
        savedScheduleDto2 = scheduleService.addSchedule(scheduleDto2, savedTeamDto2.getId());
        savedScheduleDto3 = scheduleService.addSchedule(scheduleDto3, savedTeamB.getId());
    }

    @AfterEach
    void afterEach() {
        databaseCleanUp.truncateAllEntity();
    }

    @Test
    public void findByScheduleId() {
        ScheduleDto findScheduleDto = scheduleService.findByScheduleId(savedScheduleDto1.getId()).get();
        assertThat(findScheduleDto).usingRecursiveComparison().isEqualTo(savedScheduleDto1);
    }

    @Test
    public void findByTeamId() {
        List<ScheduleDto> result = scheduleService.findByTeamId(savedTeamDto2.getId());
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).usingRecursiveFieldByFieldElementComparator().contains(savedScheduleDto1, savedScheduleDto2);
    }

    @Test
    public void addSchedule() {
        ScheduleDto scheduleDto = ScheduleDto.builder()
                .title("병원")
                .scheduleTime(LocalDateTime.now())
                .build();

        scheduleDto.getScheduleMembers().add(savedMemberDto1.getUsername());

        ScheduleDto savedScheduleDto = scheduleService.addSchedule(scheduleDto, savedTeamDto2.getId());
        assertThat(savedScheduleDto.getScheduleMembers().size()).isEqualTo(1);
    }



    @Test
    public void updateSchedule() {
        ScheduleDto updateScheduleDto = ScheduleDto.builder()
                .title("한강 산책")
                .description("🐶🐶🐶")
                .scheduleTime(LocalDateTime.of(2023, 5, 26, 19, 30))
                .build();
        List<MemberDto> memberDtos = new ArrayList<>();
        memberDtos.add(savedMemberDto1);
        memberDtos.add(savedMemberDto2);

        // update
        ScheduleDto updatedScheduleDto = scheduleService.updateSchedule(savedScheduleDto1.getId(), updateScheduleDto);

        assertThat(updatedScheduleDto.getTitle()).isEqualTo(updateScheduleDto.getTitle());
    }

    @Test
    public void removeSchedule() {
        scheduleService.removeSchedule(savedScheduleDto1.getId());
        List<ScheduleDto> result = scheduleService.findByTeamId(savedTeamDto2.getId());
        assertThat(result.size()).isEqualTo(1);
        assertThat(result).usingRecursiveFieldByFieldElementComparator().doesNotContain(savedScheduleDto1);
    }

    @Test
//    @Transactional(readOnly = true)
    public void test() {
        log.info("@Transactional(readOnly = true)");
    }
}