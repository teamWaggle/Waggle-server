package com.example.waggle.domain.chat.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.chat.ChatRoomRequest;

public interface ChatRoomCommandService {

    Long createChatRoom(Member member, ChatRoomRequest request);

    Long joinChatRoom(Member member, Long chatRoomId, String password);

    void leaveChatRoom(Member member, Long chatRoomId);

    void deleteChatRoom(Member member, Long chatRoomId);

    Long updateChatRoom(Member member, Long chatRoomId, ChatRoomRequest request);

}
