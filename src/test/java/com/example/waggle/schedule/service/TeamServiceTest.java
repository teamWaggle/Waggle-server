package com.example.waggle.schedule.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.schedule.service.TeamCommandService;
import com.example.waggle.domain.schedule.service.TeamQueryService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.member.MemberResponse;
import com.example.waggle.web.dto.schedule.TeamDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TeamServiceTest {
    @Autowired
    DatabaseCleanUp databaseCleanUp;
    @Autowired
    MemberCommandService memberService;
    @Autowired
    TeamQueryService teamService;
    @Autowired
    TeamCommandService teamCommandService;

    private MemberResponse.MemberSummaryDto savedMemberSummaryDto1;
    private MemberResponse.MemberSummaryDto savedMemberSummaryDto2;
    private Long savedTeamId1;
    private Long savedTeamId2;

    @BeforeEach
    void beforeEach() {
        // Member 회원가입
        MemberRequest.RegisterRequestDto signUpDto1 = MemberRequest.RegisterRequestDto.builder()
                .username("member1")
                .password("12345678")
                .build();
        MemberRequest.RegisterRequestDto signUpDto2 = MemberRequest.RegisterRequestDto.builder()
                .username("member2")
                .password("12345678")
                .build();

        memberService.signUp(signUpDto1, null);
        memberService.signUp(signUpDto2, null);
    }

    @AfterEach
    void afterEach() {
        databaseCleanUp.truncateAllEntity();
    }


    @Test
    @DisplayName("username을 통해 해당 유저가 속한 모든 팀 조회")
    public void findAllTeamByUsernameTest() {
        TeamDto teamA = TeamDto.builder()
                .name("teamA").build();
        TeamDto teamB = TeamDto.builder()
                .name("teamB").build();
        TeamDto teamC = TeamDto.builder()
                .name("teamC").build();

        teamCommandService.createTeam(teamA, savedMemberSummaryDto1.getUsername());
        teamCommandService.createTeam(teamB, savedMemberSummaryDto1.getUsername());

        Long teamWithMember = teamCommandService.createTeam(teamC, savedMemberSummaryDto2.getUsername());
        teamCommandService.addTeamMember(teamWithMember, savedMemberSummaryDto1.getUsername());

        List<TeamDto> allTeamByUsername = teamService.getTeamsByUsername(savedMemberSummaryDto1.getUsername());

        for (TeamDto teamDto : allTeamByUsername) {
            System.out.println("teamDto = " + teamDto);

        }
        assertThat(allTeamByUsername.size()).isEqualTo(3);

    }


    @Test
    @DisplayName("1명의 멤버를 통해 초기 팀 생성")
    public void createTeamWithMemberTest() {
        MemberRequest.RegisterRequestDto signUpDto = MemberRequest.RegisterRequestDto.builder()
                .username("test")
                .password("12345678")
                .nickname("닉네임")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        Member member = memberService.signUp(signUpDto, null);

        TeamDto teamA = TeamDto.builder()
                .name("teamA").build();

        teamCommandService.createTeam(teamA, member.getUsername());
    }

    @Test
    @DisplayName("초기 팀 생성 -> username 통해 새로운 멤버 추가")
    public void createThenAddMemberTest() {
        // Team 생성 (member1 가입)
        TeamDto team = TeamDto.builder()
                .name("team").build();

        savedTeamId1 = teamCommandService.createTeam(team, savedMemberSummaryDto1.getUsername());

        // member2 가입
        savedTeamId2 = teamCommandService.addTeamMember(savedTeamId1, savedMemberSummaryDto2.getUsername());

        // team 테이블의 전체 개수 1개
        assertThat(teamService.getTeams().size()).isEqualTo(1);

        TeamDto findTeam = teamService.getTeamById(savedTeamId1);
        assertThat(findTeam.getTeamMembers().size()).isEqualTo(2);
        assertThat(findTeam.getName()).isEqualTo("team");
    }

    @Test
    @DisplayName("초기 팀 생성 -> 새로운 멤버 추가 -> 팀 삭제")
    public void createThenAddMemberThenRemoveTeamTest() {
        // Team 생성 (member1 가입)
        TeamDto team = TeamDto.builder()
                .name("team").build();

        savedTeamId1 = teamCommandService.createTeam(team, savedMemberSummaryDto1.getUsername());

        // member2 가입
        savedTeamId2 = teamCommandService.addTeamMember(savedTeamId1, savedMemberSummaryDto2.getUsername());

        // team 삭제
        TeamDto findTeam = teamService.getTeamById(savedTeamId2);
        teamCommandService.deleteTeam(findTeam.getId());
        assertThat(teamService.getTeams().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("team id를 통해 해당 팀에 속한 멤버들 조회")
    public void findTeamMembersTest() {
        // Team 생성 (member1 가입)
        TeamDto team = TeamDto.builder()
                .name("team").build();

        savedTeamId1 = teamCommandService.createTeam(team, savedMemberSummaryDto1.getUsername());

        // member2 가입
        savedTeamId2 = teamCommandService.addTeamMember(savedTeamId1, savedMemberSummaryDto2.getUsername());

        //TODO teamMember service 로 받아오기
    }

    @Test
    @DisplayName("팀에서 username 통해 특정 멤버 제거")
    public void removeMemberTest() {
        // Team 생성 (member1 가입)
        TeamDto team = TeamDto.builder()
                .name("team").build();

        savedTeamId1 = teamCommandService.createTeam(team, savedMemberSummaryDto1.getUsername());

        // member2 가입
        savedTeamId2 = teamCommandService.addTeamMember(savedTeamId1, savedMemberSummaryDto2.getUsername());

        // member1 삭제 -> team에 속한 전체 member 수는 1이 되어야 함
        teamCommandService.deleteTeamMember(savedTeamId2, savedMemberSummaryDto1.getUsername());

        TeamDto teamDto = teamService.getTeamById(savedTeamId2);

        assertThat(teamDto.getTeamMembers().size()).isEqualTo(1);
    }
}