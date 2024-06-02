package com.example.waggle.domain.chat.persistence.dao.querydsl;

import static com.example.waggle.domain.chat.persistence.entity.QChatRoomMember.chatRoomMember;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ChatRoomQueryRepositoryImpl implements ChatRoomQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long countChatRoomsByMemberId(Long memberId) {
        return queryFactory
                .select(chatRoomMember.count())
                .from(chatRoomMember)
                .where(chatRoomMember.member.id.eq(memberId))
                .fetchFirst();
    }
}
