package com.example.waggle.domain.chat.application.message;

import com.example.waggle.domain.chat.persistence.dao.ChatMessageRepository;
import com.example.waggle.domain.chat.persistence.dao.ChatRoomMemberRepository;
import com.example.waggle.domain.chat.persistence.entity.ChatMessage;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.exception.object.handler.ChatRoomHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
