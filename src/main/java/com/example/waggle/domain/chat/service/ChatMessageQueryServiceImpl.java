package com.example.waggle.domain.chat.service;

import com.example.waggle.domain.chat.entity.ChatMessage;
import com.example.waggle.domain.chat.repository.ChatMessageRepository;
import com.example.waggle.domain.chat.repository.ChatRoomMemberRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.exception.handler.ChatRoomHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatMessageQueryServiceImpl implements ChatMessageQueryService {

    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public Page<ChatMessage> getPagedChatMessages(Member member, Long chatRoomId, Pageable pageable) {
        if (chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, member.getId()).isEmpty()) {
            throw new ChatRoomHandler(ErrorStatus.CHAT_ROOM_MEMBER_NOT_FOUND);
        }
        return chatMessageRepository.findByChatRoomIdSortedBySendTimeDesc(chatRoomId, pageable);
    }
}
