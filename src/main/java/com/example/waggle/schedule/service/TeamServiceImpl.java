package com.example.waggle.schedule.service;

import com.example.waggle.member.domain.Member;
import com.example.waggle.member.domain.TeamMember;
import com.example.waggle.member.dto.MemberDto;
import com.example.waggle.member.repository.MemberRepository;
import com.example.waggle.member.repository.TeamMemberRepository;
import com.example.waggle.schedule.domain.Team;
import com.example.waggle.schedule.dto.TeamDto;
import com.example.waggle.schedule.repository.ScheduleRepository;
import com.example.waggle.schedule.repository.TeamRepository;
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
public class TeamServiceImpl implements TeamService {
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ScheduleRepository scheduleRepository;

    // 전체 team 조회
    @Override
    public List<TeamDto> findAllTeam() {
        List<Team> result = teamRepository.findAll();
        return result.stream().map(TeamDto::toDto).collect(Collectors.toList());
    }

    @Override
    public List<TeamDto> findAllTeamByUsername(String username) {
        Optional<Member> byUsername = memberRepository.findByUsername(username);
        List<Team> teamList = new ArrayList<>();
        if(byUsername.isPresent()) {
            Member member = byUsername.get();
            List<TeamMember> teamMembers = member.getTeamMembers();
            for (TeamMember teamMember : teamMembers) {
                teamList.add(teamMember.getTeam());
            }
        }

        return teamList.stream().map(TeamDto::toDto).collect(Collectors.toList());
    }


    // teamId로 team 조회
    @Override
    public Optional<TeamDto> findByTeamId(Long teamId) {
        Optional<Team> findTeam = teamRepository.findById(teamId);
        return findTeam.map(TeamDto::toDto);
    }

    // teamId로 해당 team의 member들 조회
    @Override
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
    @Override
    @Transactional
    public TeamDto createTeamWithMember(TeamDto teamDto, String username) {
        Optional<Member> member = memberRepository.findByUsername(username);

        if (member.isPresent()) {
            Team team = teamRepository.save(teamDto.toEntity(member.get()));
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
    @Override
    @Transactional
    public TeamDto addMember(Long teamId, String username) {
        Optional<Team> optionalTeam = teamRepository.findById(teamId);
        Optional<Member> optionalMember = memberRepository.findByUsername(username);

        if (optionalTeam.isPresent() && optionalMember.isPresent()) {
            Team team = optionalTeam.get();
            Member member = optionalMember.get();

            if(validateDuplicateMember(team, member)) {
                TeamMember teamMember = TeamMember.builder()
                        .team(team)
                        .member(member).build();
                teamMember.addTeamMember(team, member);  // TeamMember 연관관계 편의 메소드
                teamMemberRepository.save(teamMember);
                return TeamDto.toDto(team);
            } else {
                // TODO 에외 처리 (중복 멤버 저장 시도)
                return null;
            }
        } else {
            // TODO 예외 처리 (member / team이 null)
            return null;
        }
    }

    // update
    @Transactional
    @Override
    public TeamDto updateTeam(Long teamId, TeamDto updateTeamDto) {
        Optional<Team> team = teamRepository.findById(teamId);
        if(team.isPresent()) {
            team.get().updateTeamName(updateTeamDto.getName());
            return TeamDto.toDto(team.get());
        } else {
            // TODO 예외 처리
            return null;
        }
    }



    // team 삭제 (team, member, team_member 삭제)
    @Transactional
    @Override
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
    @Override
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


    @Override
    public Boolean validateDuplicateMember(Team team, Member member) {
        List<TeamMember> teamMembers = team.getTeamMembers();
        for (TeamMember teamMember : teamMembers) {
            if(teamMember.getMember().equals(member)) return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean isTeamLeader(Long teamId, String username) {
        Team team = teamRepository.findById(teamId).orElse(null);
        if (team == null) {
            return Boolean.FALSE;
        }
        Member currentUser = memberRepository.findByUsername(username).get();
        return team.getTeamLeader() != null && team.getTeamLeader().equals(currentUser);
    }
}
