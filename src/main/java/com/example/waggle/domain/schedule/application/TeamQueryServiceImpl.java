package com.example.waggle.domain.schedule.application;

import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.schedule.persistence.dao.jpa.ParticipationRepository;
import com.example.waggle.domain.schedule.persistence.dao.jpa.TeamMemberRepository;
import com.example.waggle.domain.schedule.persistence.dao.jpa.TeamRepository;
import com.example.waggle.domain.schedule.persistence.entity.Participation;
import com.example.waggle.domain.schedule.persistence.entity.Team;
import com.example.waggle.domain.schedule.presentation.dto.team.Status;
import com.example.waggle.exception.object.handler.TeamHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TeamQueryServiceImpl implements TeamQueryService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ParticipationRepository participationRepository;

    @Override
    public Page<Team> getPagedTeamByMemberId(Long memberId, Pageable pageable) {
        return teamRepository.findByTeamMembers_MemberId(memberId, pageable);
    }

    @Override
    public Page<Team> getTeamByContainName(String name, Pageable pageable) {
        return teamRepository.findByNameContaining(name, pageable);
    }

    @Override
    public Page<Team> getPopularTeamListTop3(Pageable pageable) {
        return teamRepository.findBySizeOfTeamMembers(pageable);
    }


    @Override
    public Team getTeamById(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
    }

    @Override
    public List<Participation> getParticipationList(Member leader, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        validateIsLeader(leader, team);
        return participationRepository.findByTeam(team);
    }

    private static void validateIsLeader(Member leader, Team team) {
        if (!team.getLeader().equals(leader)) {
            throw new TeamHandler(ErrorStatus.TEAM_LEADER_UNAUTHORIZED);
        }
    }

    @Override
    public Status getParticipationStatus(Member member, Long teamId) {
        if (teamMemberRepository.existsByMemberIdAndTeamId(member.getId(), teamId)) {
            return Status.ACCEPTED;
        }
        if (participationRepository.existsByMemberIdAndTeamId(member.getId(), teamId)) {
            return Status.PENDING;
        }
        return Status.NONE;
    }
}
