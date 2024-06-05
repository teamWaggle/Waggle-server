package com.example.waggle.domain.chat.application;

import com.example.waggle.domain.chat.persistence.dao.ChatMessageRepository;
import com.example.waggle.domain.chat.persistence.dao.ChatRoomMemberRepository;
import com.example.waggle.domain.chat.persistence.entity.ChatMessage;
import com.example.waggle.domain.chat.persistence.entity.ChatRoomMember;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.exception.object.handler.ChatRoomHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import java.time.ZoneId;
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
        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, member.getId())
                .orElseThrow(() -> new ChatRoomHandler(ErrorStatus.CHAT_ROOM_MEMBER_NOT_FOUND));

        long memberEnterTime = chatRoomMember.getCreatedDate().atZone(ZoneId.of("Asia/Seoul")).toInstant()
                .toEpochMilli();

        return chatMessageRepository.findMessagesByChatRoomIdAfterMemberEnterTime(chatRoomId, memberEnterTime,
                pageable);
    }
}
