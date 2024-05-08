package com.example.waggle.domain.chat.application.chatRoom;

import com.example.waggle.domain.chat.presentation.dto.ChatRoomRequest;
import com.example.waggle.domain.member.persistence.entity.Member;

import java.time.LocalDateTime;

public interface ChatRoomCommandService {

    Long createChatRoom(Member member, ChatRoomRequest request);

    Long joinChatRoom(Member member, Long chatRoomId, String password);

    void leaveChatRoom(Member member, Long chatRoomId);

    void deleteChatRoom(Member member, Long chatRoomId);

    Long updateLastAccessTime(Member member, Long chatRoomId, LocalDateTime lastAccessTime);

    Long updateChatRoom(Member member, Long chatRoomId, ChatRoomRequest request);

}
