package com.example.waggle.domain.schedule.persistence.dao.team.querydsl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.waggle.domain.conversation.persistence.entity.QComment.comment;
import static com.example.waggle.domain.schedule.persistence.entity.QMemberSchedule.memberSchedule;
import static com.example.waggle.domain.schedule.persistence.entity.QParticipation.participation;
import static com.example.waggle.domain.schedule.persistence.entity.QSchedule.schedule;
import static com.example.waggle.domain.schedule.persistence.entity.QTeam.team;
import static com.example.waggle.domain.schedule.persistence.entity.QTeamMember.teamMember;

@RequiredArgsConstructor
@Repository
public class TeamQueryRepositoryImpl implements TeamQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteTeamWithRelationsByMemberId(Long memberId) {
        deleteRelatedDataAboutTeam(memberId);
        deleteRelatedDataAboutSchedule(memberId);
        queryFactory.delete(team).where(team.leader.id.eq(memberId)).execute();

    }

    @Override
    public void deleteTeamWithRelations(Long teamId) {
        queryFactory.delete(teamMember).where(teamMember.team.id.eq(teamId)).execute();
        JPAQuery<Long> scheduleSubQuery = queryFactory.select(schedule.id).from(schedule).join(schedule.team, team).where(team.id.eq(teamId));
        queryFactory.delete(memberSchedule).where(memberSchedule.schedule.id.in(scheduleSubQuery)).execute();
        queryFactory.delete(comment).where(comment.board.id.in(scheduleSubQuery)).execute();
        queryFactory.delete(participation).where(participation.team.id.eq(teamId)).execute();
        queryFactory.delete(schedule).where(schedule.id.in(scheduleSubQuery)).execute();
        queryFactory.delete(team).where(team.id.eq(teamId)).execute();
    }

    private void deleteRelatedDataAboutTeam(Long memberId) {
        JPAQuery<Long> teamSubQuery = queryFactory.select(team.id).from(team).where(team.leader.id.eq(memberId));
        queryFactory.delete(participation).where(participation.team.id.in(teamSubQuery)).execute();
        queryFactory.delete(participation).where(participation.member.id.eq(memberId)).execute();
        queryFactory.delete(teamMember).where(teamMember.member.id.eq(memberId)).execute();
        queryFactory.delete(teamMember).where(teamMember.team.id.in(teamSubQuery)).execute();
    }

    private void deleteRelatedDataAboutSchedule(Long memberId) {
        JPAQuery<Long> scheduleSubQuery = queryFactory.select(schedule.id).from(schedule).join(schedule.team, team).where(team.leader.id.eq(memberId));
        queryFactory.delete(memberSchedule).where(memberSchedule.member.id.eq(memberId)).execute();
        queryFactory.delete(memberSchedule).where(memberSchedule.schedule.id.in(scheduleSubQuery)).execute();
        queryFactory.delete(schedule).where(schedule.id.in(scheduleSubQuery)).execute();
    }
}
