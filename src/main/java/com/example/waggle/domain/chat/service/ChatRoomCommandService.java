package com.example.waggle.domain.chat.service;

import com.example.waggle.domain.chat.entity.ChatRoom;
import com.example.waggle.domain.chat.entity.ChatRoomMember;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.chat.ChatRequest;
import java.time.LocalDateTime;

public interface ChatRoomCommandService {

    ChatRoom createChatRoom(Member member, ChatRequest request);

    ChatRoomMember joinChatRoom(Member member, Long chatRoomId, String password);

    void leaveChatRoom(Member member, Long chatRoomId);

    void deleteChatRoom(Member member, Long chatRoomId);

    ChatRoom updateChatRoom(Member member, Long chatRoomId, ChatRequest request);

    Long updateLastAccessTime(Member member, Long chatRoomId, LocalDateTime lastAccessTime);

}
