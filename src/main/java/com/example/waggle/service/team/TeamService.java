package com.example.waggle.service.team;

import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.team.Team;
import com.example.waggle.domain.team.TeamMember;
import com.example.waggle.dto.member.*;
import com.example.waggle.repository.member.MemberRepository;
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

    // 전체 team 조회
    List<TeamDto> findAllTeam() {
        List<Team> result = teamRepository.findAll();
        return result.stream()
                .map(TeamDto::toDto)
                .collect(Collectors.toList());
    }

    // teamId로 team 조회
    Optional<TeamDto> findByTeamId(Long teamId) {
        Optional<Team> findTeam = teamRepository.findById(teamId);
        return findTeam.map(TeamDto::toDto);
    }

    // teamId로 해당 team의 member들 조회
    List<MemberDto> findTeamMembers(Long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        List<MemberDto> result = new ArrayList<>();

        team.ifPresent(t -> {
            List<TeamMember> teamMembers = t.getTeamMembers();
            for (TeamMember teamMember : teamMembers) {
                Member member = teamMember.getMember();
                MemberDto memberDto = MemberDto.toDto(member);
                result.add(memberDto);
            }
        });
        return result;
    }

    // 새로운 team 생성 (대표 member 한 명 추가)
    @Transactional
    TeamDto createTeamWithMember(TeamDto teamDto, MemberDto memberDto) {
        Team savedTeam = teamRepository.save(teamDto.toEntity());

        TeamMember teamMember = new TeamMember();
        teamMember.setTeam(savedTeam);
        teamMember.setMember(memberDto.toEntity());

        teamMemberRepository.save(teamMember);

        return TeamDto.toDto(savedTeam);
    }

    // 기존 team에 새로운 member 추가
    @Transactional
    TeamDto addMember(Long teamId, String username) {
        Optional<Team> team = teamRepository.findById(teamId);
        Optional<Member> member = memberRepository.findByUsername(username);

        if (team.isPresent() && member.isPresent()) {
            TeamMember teamMember = new TeamMember();
            teamMember.setTeam(team.get());
            teamMember.setMember(member.get());
            teamMemberRepository.save(teamMember);
            return TeamDto.toDto(team.get());
        }
        return null;
    }

    // team 삭제 (team, member, team_member 삭제)
    @Transactional
    Boolean removeTeam(Long teamId) {
        Optional<Team> removalTeam = teamRepository.findById(teamId);
        if(removalTeam.isPresent()) {
            Team team = removalTeam.get();
            List<TeamMember> teamMembers = new ArrayList<>(team.getTeamMembers());  // ConcurrentModificationException 방지하기 위하여

            for (TeamMember teamMember : teamMembers) {

                Optional<Member> findMember = memberRepository.findByTeamMembers(teamMember);
                if (findMember.isPresent()) {
                    Member member = findMember.get();
                    member.getTeamMembers().remove(teamMember);
                    memberRepository.save(member);
                }
                Optional<Team> findTeam = teamRepository.findByTeamMembers(teamMember);
                if (findTeam.isPresent()) {
                    Team t = findTeam.get();
                    t.getTeamMembers().remove(teamMember);
                    teamRepository.save(t);
                }
                teamMemberRepository.delete(teamMember);
            }
            // 팀 삭제
            teamRepository.delete(team);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    // team의 member 삭제
    @Transactional
    Boolean removeMember(Long teamId, String username) {
        // member가 한 명밖에 없으면 해당 팀도 삭제
        Optional<Team> team = teamRepository.findById(teamId);
        Optional<Member> member = memberRepository.findByUsername(username);

        if (team.isPresent() && member.isPresent()) {
            Team findTeam = team.get();
            Member findMember = member.get();

            // team 통해서 teamMember list 가져옴
            List<TeamMember> teamMembers = findTeam.getTeamMembers();

            // teamMember list에서 우리가 찾는 member 찾아옴
            Optional<TeamMember> teamMemberToRemove = teamMembers.stream()
                    .filter(teamMember -> teamMember.getMember().equals(findMember))
                    .findFirst();

            teamMemberToRemove.ifPresent(teamMember -> {
                teamMembers.remove(teamMember);
                findMember.getTeamMembers().remove(teamMember);
                teamMemberRepository.delete(teamMember);

                teamRepository.save(findTeam);
                memberRepository.save(findMember);
            });
            // member가 비어있는 team 삭제
            if (teamMembers.isEmpty()) {
                teamRepository.delete(findTeam);
            }
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
