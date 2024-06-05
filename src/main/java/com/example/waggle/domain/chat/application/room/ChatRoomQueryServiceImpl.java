package com.example.waggle.domain.chat.application.room;

import com.example.waggle.domain.chat.persistence.dao.ChatMessageRepository;
import com.example.waggle.domain.chat.persistence.dao.ChatRoomMemberRepository;
import com.example.waggle.domain.chat.persistence.dao.ChatRoomRepository;
import com.example.waggle.domain.chat.persistence.entity.ChatMessage;
import com.example.waggle.domain.chat.persistence.entity.ChatRoom;
import com.example.waggle.domain.chat.persistence.entity.ChatRoomMember;
import com.example.waggle.domain.member.persistence.dao.jpa.MemberRepository;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.exception.object.handler.ChatRoomHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.global.util.MediaUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatRoomQueryServiceImpl implements ChatRoomQueryService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MemberRepository memberRepository;

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
        Long unreadCount = chatMessageRepository.countByChatRoomIdAndSendTimeAfter(
                chatRoomId,
                chatRoomMember.getLastAccessTime().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli());
        return unreadCount;
    }

    @Override
    public String getLastMessageContent(Long chatRoomId) {
        Page<ChatMessage> pagedChatMessage = chatMessageRepository.findRecentMessagesByChatRoomId(
                chatRoomId, PageRequest.of(0, 1));
        if (!pagedChatMessage.hasContent()) {
            return "최근 메시지가 없습니다.";
        }
        return pagedChatMessage.getContent().get(0).getContent();
    }

    @Override
    public String getLastSenderProfileImgUrl(Long chatRoomId) {
        return findLastChatMessageByChatRoomId(chatRoomId)
                .map(ChatMessage::getSenderUserUrl)
                .flatMap(this::findProfileImgUrlByUserUrl)
                .orElse(MediaUtil.getDefaultMemberProfileImgUrl());
    }

    @Override
    public Long countChatRoomsByMemberId(Long memberId) {
        return chatRoomRepository.countChatRoomsByMemberId(memberId);
    }

    @Override
    public Page<ChatRoom> getPagedChatRoomListByKeyword(String keyword, Pageable pageable) {
        return chatRoomRepository.findChatRoomsByKeyword(keyword, pageable);
    }

    @Override
    public Page<ChatRoom> getPagedChatRoomListByKeywordExcludingMember(Member member, String keyword,
                                                                       Pageable pageable) {
        return chatRoomRepository.findChatRoomsByKeywordExcludingMember(member, keyword, pageable);
    }

    private Optional<ChatMessage> findLastChatMessageByChatRoomId(Long chatRoomId) {
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<ChatMessage> pagedChatMessage = chatMessageRepository.findRecentMessagesByChatRoomId(chatRoomId,
                pageRequest);
        return pagedChatMessage.getContent().stream().findFirst();
    }

    private Optional<String> findProfileImgUrlByUserUrl(String userUrl) {
        return memberRepository.findByUserUrl(userUrl)
                .map(member -> getValidProfileImgUrlOrDefault(member));
    }

    private String getValidProfileImgUrlOrDefault(Member member) {
        return Optional.ofNullable(member.getProfileImgUrl())
                .filter(url -> !url.isEmpty())
                .map(MediaUtil::appendUri)
                .orElse(MediaUtil.getDefaultMemberProfileImgUrl());
    }
}