package com.example.waggle.domain.chat.service;

import com.example.waggle.domain.chat.entity.ChatMessage;
import com.example.waggle.domain.chat.entity.ChatRoom;
import com.example.waggle.domain.chat.entity.ChatRoomMember;
import com.example.waggle.domain.chat.repository.ChatMessageRepository;
import com.example.waggle.domain.chat.repository.ChatRoomMemberRepository;
import com.example.waggle.domain.chat.repository.ChatRoomRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.exception.handler.ChatRoomHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatRoomQueryServiceImpl implements ChatRoomQueryService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Override
    public List<ChatRoom> getChatRooms() {
        return chatRoomRepository.findAll();
    }

    @Override
    public Page<ChatRoom> getPagedChatRooms(Pageable pageable) {
        return chatRoomRepository.findAll(pageable);
    }

    @Override
    public ChatRoom getChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomHandler(ErrorStatus.CHAT_ROOM_NOT_FOUND));
    }

    @Override
    public Page<ChatRoom> getPagedActiveChatRoomsByMember(Member member, Pageable pageable) {
        return chatRoomRepository.findByMemberId(member.getId(), pageable);
    }

    @Override
    public long getUnreadMessagesCount(Member member, Long chatRoomId) {
        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, member.getId())
                .orElseThrow(() -> new ChatRoomHandler(ErrorStatus.CHAT_ROOM_MEMBER_NOT_FOUND));
        return chatMessageRepository.countByChatRoomIdAndSendTimeAfter(chatRoomId, chatRoomMember.getLastAccessTime());
    }

    @Override
    public String getLastMessageContent(Long chatRoomId) {
        return chatMessageRepository.findTopByChatRoomIdOrderBySendTimeDesc(chatRoomId)
                .map(ChatMessage::getContent)
                .orElse("No recent messages");
    }


}
