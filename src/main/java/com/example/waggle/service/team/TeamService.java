package com.example.waggle.service.team;

import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.team.Schedule;
import com.example.waggle.domain.team.ScheduleMember;
import com.example.waggle.domain.team.Team;
import com.example.waggle.domain.team.TeamMember;
import com.example.waggle.dto.member.*;
import com.example.waggle.repository.member.MemberRepository;
import com.example.waggle.repository.team.ScheduleRepository;
import com.example.waggle.repository.team.TeamMemberRepository;
import com.example.waggle.repository.team.TeamRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TeamService {
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ScheduleRepository scheduleRepository;

    // 전체 team 조회
    public List<TeamDto> findAllTeam() {
        List<Team> result = teamRepository.findAll();
        return result.stream().map(TeamDto::toDto).collect(Collectors.toList());
    }

    // teamId로 team 조회
    public Optional<TeamDto> findByTeamId(Long teamId) {
        Optional<Team> findTeam = teamRepository.findById(teamId);
        return findTeam.map(TeamDto::toDto);
    }

    // teamId로 해당 team의 member들 조회
    public List<MemberDto> findTeamMembers(Long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        List<MemberDto> result = new ArrayList<>();

        if (team.isPresent()) {
            List<TeamMember> teamMembers = team.get().getTeamMembers();
            for (TeamMember teamMember : teamMembers) {
                Member member = teamMember.getMember();
                MemberDto memberDto = MemberDto.toDto(member);
                result.add(memberDto);
            }
        }
        return result;
    }

    // 새로운 team 생성 (대표 member 한 명 추가)
    @Transactional
    public TeamDto createTeamWithMember(TeamDto teamDto, MemberDto memberDto) {
        Optional<Member> member = memberRepository.findByUsername(memberDto.getUsername());

        if (member.isPresent()) {
            Team team = teamRepository.save(teamDto.toEntity());
            TeamMember teamMember = TeamMember.builder()
                    .team(team)
                    .member(member.get()).build();
            teamMember.addTeamMember(team, member.get());  // TeamMember 연관관계 편의 메소드
            teamMemberRepository.save(teamMember);
            return TeamDto.toDto(team);
        } else {
            // TODO 예외 처리
            return null;
        }
    }

    // 기존 team에 새로운 member 추가
    @Transactional
    public TeamDto addMember(Long teamId, String username) {
        Optional<Team> team = teamRepository.findById(teamId);
        Optional<Member> member = memberRepository.findByUsername(username);

        if (team.isPresent() && member.isPresent()) {
            TeamMember teamMember = TeamMember.builder()
                    .team(team.get())
                    .member(member.get()).build();
            teamMember.addTeamMember(team.get(), member.get());  // TeamMember 연관관계 편의 메소드
            teamMemberRepository.save(teamMember);
            return TeamDto.toDto(team.get());
        } else {
            // TODO 예외 처리
            return null;
        }
    }

    // team 삭제 (team, member, team_member 삭제)
    @Transactional
    public Boolean removeTeam(Long teamId) {
        Optional<Team> teamToRemove = teamRepository.findById(teamId);
        if (teamToRemove.isPresent()) {
            Team team = teamToRemove.get();
            List<TeamMember> teamMembers = team.getTeamMembers();
            for (TeamMember teamMember : teamMembers) {
                teamMemberRepository.delete(teamMember);
            }
            teamRepository.delete(team);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    // team의 member 삭제
    @Transactional
    public Boolean removeMember(Long teamId, String username) {
        Optional<Team> team = teamRepository.findById(teamId);
        Optional<TeamMember> teamMemberToRemove = teamMemberRepository.findTeamMemberByMemberUsernameAndTeamId(username, teamId);
        if (team.isPresent() && teamMemberToRemove.isPresent()) {
            TeamMember teamMember = teamMemberToRemove.get();
            teamMember.removeTeam();
            teamMemberRepository.delete(teamMember);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
