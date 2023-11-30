package com.example.waggle.schedule.service;

import static com.example.waggle.commons.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.example.waggle.commons.exception.ErrorCode.TEAM_NOT_FOUND;

import com.example.waggle.commons.exception.CustomAlertException;
import com.example.waggle.member.domain.Member;
import com.example.waggle.member.domain.TeamMember;
import com.example.waggle.member.repository.MemberRepository;
import com.example.waggle.member.repository.TeamMemberRepository;
import com.example.waggle.schedule.domain.Team;
import com.example.waggle.schedule.dto.TeamDto;
import com.example.waggle.schedule.repository.TeamRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TeamCommandServiceImpl implements TeamCommandService{

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Override
    public Long createTeam(TeamDto teamDto, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomAlertException(MEMBER_NOT_FOUND));

        Team team = teamRepository.save(teamDto.toEntity(member));
        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .member(member).build();
        teamMember.addTeamMember(team, member);
        teamMemberRepository.save(teamMember);
        return team.getId();
    }

    @Override
    public Long addMember(Long teamId, String username) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomAlertException(TEAM_NOT_FOUND));
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomAlertException(MEMBER_NOT_FOUND));

        if (validateMemberDuplication(team, member)) {
            TeamMember teamMember = TeamMember.builder()
                    .team(team)
                    .member(member).build();
            teamMember.addTeamMember(team, member);
            teamMemberRepository.save(teamMember);
        } else {
            // TODO 중복 회원 저장 예외
        }
        return team.getId();
    }

    @Override
    public Long updateTeam(Long teamId, TeamDto updateTeamDto) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomAlertException(TEAM_NOT_FOUND));
        team.updateTeamName(updateTeamDto.getName());
        return team.getId();
    }

    @Override
    public void deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomAlertException(TEAM_NOT_FOUND));

        List<TeamMember> teamMembers = team.getTeamMembers();
        for (TeamMember teamMember : teamMembers) {
            teamMemberRepository.delete(teamMember);
        }
        teamRepository.delete(team);
    }

    @Override
    public void removeMember(Long teamId, String username) {
        TeamMember teamMember = teamMemberRepository.findTeamMemberByMemberUsernameAndTeamId(username, teamId)
                .orElseThrow(() -> new CustomAlertException(MEMBER_NOT_FOUND));
        teamMember.removeTeam();
        teamMemberRepository.delete(teamMember);
    }

    private boolean validateMemberDuplication(Team team, Member member) {
        List<TeamMember> teamMembers = team.getTeamMembers();
        for (TeamMember teamMember : teamMembers) {
            if (teamMember.getMember().equals(member)) return false;
        }
        return true;
    }

}
