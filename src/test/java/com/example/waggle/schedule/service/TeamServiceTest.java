package com.example.waggle.schedule.service;

import com.example.waggle.commons.component.DatabaseCleanUp;
import com.example.waggle.member.dto.MemberSummaryDto;
import com.example.waggle.member.dto.SignUpDto;
import com.example.waggle.schedule.dto.TeamDto;
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
    MemberService memberService;
    @Autowired
    TeamService teamService;

    private MemberSummaryDto savedMemberSummaryDto1;
    private MemberSummaryDto savedMemberSummaryDto2;
    private Long savedTeamId1;
    private Long savedTeamId2;

    @BeforeEach
    void beforeEach() {
        // Member 회원가입
        SignUpDto signUpDto1 = SignUpDto.builder()
                .username("member1")
                .password("12345678")
                .build();
        SignUpDto signUpDto2 = SignUpDto.builder()
                .username("member2")
                .password("12345678")
                .build();

        savedMemberSummaryDto1 = memberService.signUp(signUpDto1, null);
        savedMemberSummaryDto2 = memberService.signUp(signUpDto2, null);
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

        teamService.createTeam(teamA, savedMemberSummaryDto1.getUsername());
        teamService.createTeam(teamB, savedMemberSummaryDto1.getUsername());

        Long teamWithMember = teamService.createTeam(teamC, savedMemberSummaryDto2.getUsername());
        teamService.addMember(teamWithMember, savedMemberSummaryDto1.getUsername());

        List<TeamDto> allTeamByUsername = teamService.getTeamsByUsername(savedMemberSummaryDto1.getUsername());

        for (TeamDto teamDto : allTeamByUsername) {
            System.out.println("teamDto = " + teamDto);

        }
        assertThat(allTeamByUsername.size()).isEqualTo(3);

    }


    @Test
    @DisplayName("1명의 멤버를 통해 초기 팀 생성")
    public void createTeamWithMemberTest() {
        SignUpDto signUpDto = SignUpDto.builder()
                .username("test")
                .password("12345678")
                .nickname("닉네임")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        MemberSummaryDto savedMemberSummaryDto = memberService.signUp(signUpDto, null);

        TeamDto teamA = TeamDto.builder()
                .name("teamA").build();

        teamService.createTeam(teamA, savedMemberSummaryDto.getUsername());
    }

    @Test
    @DisplayName("초기 팀 생성 -> username 통해 새로운 멤버 추가")
    public void createThenAddMemberTest() {
        // Team 생성 (member1 가입)
        TeamDto team = TeamDto.builder()
                .name("team").build();

        savedTeamId1 = teamService.createTeam(team, savedMemberSummaryDto1.getUsername());

        // member2 가입
        savedTeamId2 = teamService.addMember(savedTeamId1, savedMemberSummaryDto2.getUsername());

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

        savedTeamId1 = teamService.createTeam(team, savedMemberSummaryDto1.getUsername());

        // member2 가입
        savedTeamId2 = teamService.addMember(savedTeamId1, savedMemberSummaryDto2.getUsername());

        // team 삭제
        TeamDto findTeam = teamService.getTeamById(savedTeamId2);
        teamService.deleteTeam(findTeam.getId());
        assertThat(teamService.getTeams().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("team id를 통해 해당 팀에 속한 멤버들 조회")
    public void findTeamMembersTest() {
        // Team 생성 (member1 가입)
        TeamDto team = TeamDto.builder()
                .name("team").build();

        savedTeamId1 = teamService.createTeam(team, savedMemberSummaryDto1.getUsername());

        // member2 가입
        savedTeamId2 = teamService.addMember(savedTeamId1, savedMemberSummaryDto2.getUsername());

        // team member 조회
        List<MemberSummaryDto> teamMembers = teamService.getTeamMembers(savedTeamId2);
        assertThat(teamMembers.size()).isEqualTo(2);

        assertThat(teamMembers).usingRecursiveFieldByFieldElementComparator().contains(savedMemberSummaryDto1, savedMemberSummaryDto2); // MemberDto에 @EqualsAndHashCode
    }

    @Test
    @DisplayName("팀에서 username 통해 특정 멤버 제거")
    public void removeMemberTest() {
        // Team 생성 (member1 가입)
        TeamDto team = TeamDto.builder()
                .name("team").build();

        savedTeamId1 = teamService.createTeam(team, savedMemberSummaryDto1.getUsername());

        // member2 가입
        savedTeamId2 = teamService.addMember(savedTeamId1, savedMemberSummaryDto2.getUsername());

        // member1 삭제 -> team에 속한 전체 member 수는 1이 되어야 함
        teamService.removeMember(savedTeamId2, savedMemberSummaryDto1.getUsername());

        TeamDto teamDto = teamService.getTeamById(savedTeamId2);

        assertThat(teamDto.getTeamMembers().size()).isEqualTo(1);
    }
}