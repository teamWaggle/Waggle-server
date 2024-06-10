package com.example.waggle.domain.chat.persistence.dao.querydsl;

import static com.example.waggle.domain.chat.persistence.entity.QChatRoom.chatRoom;
import static com.example.waggle.domain.chat.persistence.entity.QChatRoomMember.chatRoomMember;

import com.example.waggle.domain.chat.persistence.entity.ChatRoom;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ChatRoomQueryRepositoryImpl implements ChatRoomQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChatRoom> findByMemberId(Long memberId, Pageable pageable) {
        List<ChatRoom> chatRooms = queryFactory.selectFrom(chatRoom)
                .join(chatRoom.chatRoomMembers, chatRoomMember)
                .where(chatRoomMember.member.id.eq(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(chatRoom.count())
                .from(chatRoom)
                .join(chatRoom.chatRoomMembers, chatRoomMember)
                .where(chatRoomMember.member.id.eq(memberId));

        return PageableExecutionUtils.getPage(chatRooms, pageable, countQuery::fetchOne);
    }

    @Override
    public Long countChatRoomsByMemberId(Long memberId) {
        return queryFactory.select(chatRoomMember.count())
                .from(chatRoomMember)
                .where(chatRoomMember.member.id.eq(memberId))
                .fetchFirst();
    }

    @Override
    public Page<ChatRoom> findChatRoomsByKeyword(String keyword, Pageable pageable) {
        JPAQuery<ChatRoom> baseQuery = queryFactory.selectFrom(chatRoom);
        JPAQuery<Long> countQuery = queryFactory.select(chatRoom.count()).from(chatRoom);

        if (!(keyword == null || keyword.isEmpty())) {
            baseQuery.where(chatRoom.name.contains(keyword).or(chatRoom.description.contains(keyword)));
            countQuery.where(chatRoom.name.contains(keyword).or(chatRoom.description.contains(keyword)));
        }

        List<ChatRoom> chatRooms = baseQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(chatRooms, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<ChatRoom> findChatRoomsByKeywordExcludingMember(Member member,
                                                                String keyword,
                                                                Pageable pageable) {
        JPAQuery<ChatRoom> baseQuery = queryFactory.selectFrom(chatRoom);
        JPAQuery<Long> countQuery = queryFactory.select(chatRoom.count()).from(chatRoom);

        if (!(keyword == null || keyword.isEmpty())) {
            baseQuery.where(chatRoom.name.contains(keyword).or(chatRoom.description.contains(keyword)));
            countQuery.where(chatRoom.name.contains(keyword).or(chatRoom.description.contains(keyword)));
        }

        baseQuery.join(chatRoom.chatRoomMembers, chatRoomMember)
                .where(chatRoomMember.member.id.ne(member.getId()).or(chatRoomMember.member.id.isNull()));
        countQuery.join(chatRoom.chatRoomMembers, chatRoomMember)
                .where(chatRoomMember.member.id.ne(member.getId()).or(chatRoomMember.member.id.isNull()));

        List<ChatRoom> chatRooms = baseQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(chatRooms, pageable, countQuery::fetchOne);
    }


}
