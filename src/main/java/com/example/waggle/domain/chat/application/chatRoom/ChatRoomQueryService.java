package com.example.waggle.domain.chat.application.chatRoom;

import com.example.waggle.domain.chat.persistence.entity.ChatRoom;
import com.example.waggle.domain.member.persistence.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatRoomQueryService {

    List<ChatRoom> getChatRooms();

    Page<ChatRoom> getPagedChatRooms(Pageable pageable);

    ChatRoom getChatRoomById(Long chatRoomId);

    Page<ChatRoom> getPagedActiveChatRoomsByMember(Member member, Pageable pageable);

    long getUnreadMessagesCount(Member member, Long chatRoomId);

    String getLastMessageContent(Long chatRoomId);

}
