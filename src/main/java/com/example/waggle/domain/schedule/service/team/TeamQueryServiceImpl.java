package com.example.waggle.domain.schedule.service.team;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.schedule.entity.Participation;
import com.example.waggle.domain.schedule.entity.ParticipationStatus;
import com.example.waggle.domain.schedule.entity.Team;
import com.example.waggle.domain.schedule.repository.ParticipationRepository;
import com.example.waggle.domain.schedule.repository.TeamRepository;
import com.example.waggle.global.exception.handler.TeamHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TeamQueryServiceImpl implements TeamQueryService {

    private final TeamRepository teamRepository;
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
        if (!team.getLeader().equals(leader)) {
            throw new TeamHandler(ErrorStatus.TEAM_LEADER_UNAUTHORIZED);
        }
        return participationRepository.findByTeamAndStatus(team, ParticipationStatus.PENDING);
    }

    @Override
    public Optional<Participation> getParticipation(Member member, Long teamId) {
        return participationRepository.findByTeamIdAndMemberId(teamId, member.getId());
    }

    @Override
    public boolean isMemberOfTeam(Member member, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        return team.getTeamMembers().stream()
                .anyMatch(teamMember -> teamMember.getMember().equals(member));
    }
}
