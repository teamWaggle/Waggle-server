package com.example.waggle.domain.chat.service;

import com.example.waggle.domain.chat.entity.ChatRoom;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomQueryService {

    List<ChatRoom> getChatRooms();

    Page<ChatRoom> getPagedChatRooms(Pageable pageable);

    ChatRoom getChatRoomById(Long chatRoomId);

}
