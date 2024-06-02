package com.example.waggle.domain.chat.persistence.dao.querydsl;

public interface ChatRoomQueryRepository {

    Long countChatRoomsByMemberId(Long memberId);

}