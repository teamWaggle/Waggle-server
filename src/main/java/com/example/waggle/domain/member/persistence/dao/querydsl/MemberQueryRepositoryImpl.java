package com.example.waggle.domain.member.persistence.dao.querydsl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.waggle.domain.follow.persistence.entity.QFollow.follow;
import static com.example.waggle.domain.member.persistence.entity.QMember.member;
import static com.example.waggle.domain.notification.persistence.entity.QNotification.notification;
import static com.example.waggle.domain.pet.persistence.entity.QPet.pet;
import static com.example.waggle.domain.schedule.persistence.entity.QMemberSchedule.memberSchedule;
import static com.example.waggle.domain.schedule.persistence.entity.QParticipation.participation;
import static com.example.waggle.domain.schedule.persistence.entity.QSchedule.schedule;
import static com.example.waggle.domain.schedule.persistence.entity.QTeam.team;
import static com.example.waggle.domain.schedule.persistence.entity.QTeamMember.teamMember;

@RequiredArgsConstructor
@Repository
public class MemberQueryRepositoryImpl implements MemberQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteMemberWithRelations(Long memberId) {
        queryFactory.delete(follow)
                .where(follow.toMember.id.eq(memberId).or(follow.fromMember.id.eq(memberId))).execute();
        queryFactory.delete(notification)
                .where(notification.receiverId.eq(memberId).or(notification.sender.id.eq(memberId))).execute();
        queryFactory.delete(pet).where(pet.member.id.eq(memberId)).execute();
        JPAQuery<Long> teamSububQuery = queryFactory.select(team.id).from(team).where(team.leader.id.eq(memberId));
        queryFactory.delete(participation).where(participation.team.id.in(teamSububQuery)).execute();
        queryFactory.delete(teamMember).where(teamMember.team.id.in(teamSububQuery)).execute();
        JPAQuery<Long> scheduleSubQuery = queryFactory.select(schedule.id).from(schedule).join(schedule.team, team).where(team.leader.id.eq(memberId));
        queryFactory.delete(memberSchedule).where(memberSchedule.schedule.id.in(scheduleSubQuery)).execute();
        queryFactory.delete(schedule).where(schedule.id.in(scheduleSubQuery)).execute();
        queryFactory.delete(team).where(team.leader.id.eq(memberId)).execute();
        queryFactory.delete(participation).where(participation.member.id.eq(memberId)).execute();
        //TODO relations of chat
        queryFactory.delete(member).where(member.id.eq(memberId)).execute();
    }
}
