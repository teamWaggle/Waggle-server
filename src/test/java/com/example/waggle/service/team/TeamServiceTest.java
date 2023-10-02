package com.example.waggle.service.team;

import com.example.waggle.commons.component.DatabaseCleanUp;
import com.example.waggle.member.dto.MemberDetailDto;
import com.example.waggle.member.dto.SignUpDto;
import com.example.waggle.schedule.dto.TeamDto;
import com.example.waggle.member.service.MemberService;
import com.example.waggle.schedule.service.TeamService;
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

    private MemberDetailDto savedMemberDetailDto1;
    private MemberDetailDto savedMemberDetailDto2;
    private TeamDto savedTeamDto1;
    private TeamDto savedTeamDto2;

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

        savedMemberDetailDto1 = memberService.signUp(signUpDto1, null);
        savedMemberDetailDto2 = memberService.signUp(signUpDto2, null);
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

        teamService.createTeam(teamA, savedMemberDetailDto1.getUsername());
        teamService.createTeam(teamB, savedMemberDetailDto1.getUsername());

        TeamDto teamWithMember = teamService.createTeam(teamC, savedMemberDetailDto2.getUsername());
        teamService.addMember(teamWithMember.getId(), savedMemberDetailDto1.getUsername());

        List<TeamDto> allTeamByUsername = teamService.getTeamsByUsername(savedMemberDetailDto1.getUsername());

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
        MemberDetailDto savedMemberDetailDto = memberService.signUp(signUpDto, null);

        TeamDto teamA = TeamDto.builder()
                .name("teamA").build();

        teamService.createTeam(teamA, savedMemberDetailDto.getUsername());
    }

    @Test
    @DisplayName("초기 팀 생성 -> username 통해 새로운 멤버 추가")
    public void createThenAddMemberTest() {
        // Team 생성 (member1 가입)
        TeamDto team = TeamDto.builder()
                .name("team").build();

        savedTeamDto1 = teamService.createTeam(team, savedMemberDetailDto1.getUsername());

        // member2 가입
        savedTeamDto2 = teamService.addMember(savedTeamDto1.getId(), savedMemberDetailDto2.getUsername());

        // team 테이블의 전체 개수 1개
        assertThat(teamService.getTeams().size()).isEqualTo(1);

        TeamDto findTeam = teamService.getTeamById(savedTeamDto1.getId());
        assertThat(findTeam.getTeamMembers().size()).isEqualTo(2);
        assertThat(findTeam.getName()).isEqualTo("team");
    }

    @Test
    @DisplayName("초기 팀 생성 -> 새로운 멤버 추가 -> 팀 삭제")
    public void createThenAddMemberThenRemoveTeamTest() {
        // Team 생성 (member1 가입)
        TeamDto team = TeamDto.builder()
                .name("team").build();

        savedTeamDto1 = teamService.createTeam(team, savedMemberDetailDto1.getUsername());

        // member2 가입
        savedTeamDto2 = teamService.addMember(savedTeamDto1.getId(), savedMemberDetailDto2.getUsername());

        // team 삭제
        TeamDto findTeam = teamService.getTeamById(savedTeamDto2.getId());
        teamService.deleteTeam(findTeam.getId());
        assertThat(teamService.getTeams().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("team id를 통해 해당 팀에 속한 멤버들 조회")
    public void findTeamMembersTest() {
        // Team 생성 (member1 가입)
        TeamDto team = TeamDto.builder()
                .name("team").build();

        savedTeamDto1 = teamService.createTeam(team, savedMemberDetailDto1.getUsername());

        // member2 가입
        savedTeamDto2 = teamService.addMember(savedTeamDto1.getId(), savedMemberDetailDto2.getUsername());

        // team member 조회
        List<MemberDetailDto> teamMembers = teamService.getTeamMembers(savedTeamDto2.getId());
        assertThat(teamMembers.size()).isEqualTo(2);

        assertThat(teamMembers).usingRecursiveFieldByFieldElementComparator().contains(savedMemberDetailDto1, savedMemberDetailDto2); // MemberDto에 @EqualsAndHashCode
    }

    @Test
    @DisplayName("팀에서 username 통해 특정 멤버 제거")
    public void removeMemberTest() {
        // Team 생성 (member1 가입)
        TeamDto team = TeamDto.builder()
                .name("team").build();

        savedTeamDto1 = teamService.createTeam(team, savedMemberDetailDto1.getUsername());

        // member2 가입
        savedTeamDto2 = teamService.addMember(savedTeamDto1.getId(), savedMemberDetailDto2.getUsername());

        // member1 삭제 -> team에 속한 전체 member 수는 1이 되어야 함
        teamService.removeMember(savedTeamDto2.getId(), savedMemberDetailDto1.getUsername());

        TeamDto teamDto = teamService.getTeamById(savedTeamDto2.getId());

        assertThat(teamDto.getTeamMembers().size()).isEqualTo(1);
    }
}