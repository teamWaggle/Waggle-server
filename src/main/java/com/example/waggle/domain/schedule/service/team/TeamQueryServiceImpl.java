package com.example.waggle.domain.schedule.service.team;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.schedule.entity.Participation;
import com.example.waggle.domain.schedule.entity.Team;
import com.example.waggle.domain.schedule.repository.ParticipationRepository;
import com.example.waggle.domain.schedule.repository.TeamRepository;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.exception.handler.TeamHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TeamQueryServiceImpl implements TeamQueryService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final ParticipationRepository participationRepository;

    @Override
    public Page<Team> getPagingTeamByUsername(String username, Pageable pageable) {
        return teamRepository.findByTeamMembers_Member_Username(username, pageable);
    }

    @Override
    public Page<Team> getPagedTeamByMemberId(Long memberId, Pageable pageable) {
        return teamRepository.findByTeamMembers_MemberId(memberId, pageable);
    }

    @Override
    public List<Team> getTeamListByUsername(String username) {
        return teamRepository.findListByTeamMembers_Member_Username(username);
    }

    @Override
    public Team getTeamById(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
    }

    @Override
    public boolean isTeamLeader(Long teamId, String username) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return team.getLeader() != null && team.getLeader().equals(member);
    }

    @Override
    public List<Participation> getParticipationList(Member leader, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamHandler(ErrorStatus.TEAM_NOT_FOUND));
        if (!team.getLeader().equals(leader)) {
            throw new TeamHandler(ErrorStatus.TEAM_LEADER_UNAUTHORIZED);
        }
        return participationRepository.findByTeam(team);
    }

    @Override
    public Optional<Participation> getParticipation(Member member, Long teamId) {
        return participationRepository.findByTeamIdAndMemberId(teamId, member.getId());
    }
}
