package com.example.waggle.domain.member.persistence.dao.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.waggle.domain.follow.persistence.entity.QFollow.follow;
import static com.example.waggle.domain.member.persistence.entity.QMember.member;
import static com.example.waggle.domain.notification.persistence.entity.QNotification.notification;
import static com.example.waggle.domain.pet.persistence.entity.QPet.pet;

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
        //TODO relations of chat
        queryFactory.delete(member).where(member.id.eq(memberId)).execute();
    }
}
