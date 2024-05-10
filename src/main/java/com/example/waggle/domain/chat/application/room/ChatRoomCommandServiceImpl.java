package com.example.waggle.domain.chat.application.room;

import com.example.waggle.domain.chat.persistence.dao.ChatRoomMemberRepository;
import com.example.waggle.domain.chat.persistence.dao.ChatRoomRepository;
import com.example.waggle.domain.chat.persistence.entity.ChatRoom;
import com.example.waggle.domain.chat.persistence.entity.ChatRoomMember;
import com.example.waggle.domain.chat.presentation.dto.ChatRoomRequest;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.exception.object.handler.ChatRoomHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatRoomCommandServiceImpl implements ChatRoomCommandService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Override
    public Long createChatRoom(Member member, ChatRoomRequest request) {
        ChatRoom chatRoom = chatRoomRepository.save(buildChatRoom(member, request));
        addMemberToChatRoom(member, chatRoom);
        return chatRoom.getId();
    }

    private ChatRoom buildChatRoom(Member member, ChatRoomRequest request) {
        return ChatRoom.builder()
                .owner(member)
                .name(request.getName())
                .description(request.getDescription())
                .password(request.getPassword())
                .build();
    }

    @Override
    public Long joinChatRoom(Member member, Long chatRoomId, String password) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomHandler(ErrorStatus.CHAT_ROOM_NOT_FOUND));
        checkIfMemberAlreadyInChatRoom(member, chatRoom);
        validateChatRoomPassword(chatRoom, password);
        return addMemberToChatRoom(member, chatRoom).getId();
    }

    private void checkIfMemberAlreadyInChatRoom(Member member, ChatRoom chatRoom) {
        chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoom.getId(), member.getId())
                .ifPresent(m -> {
                    throw new ChatRoomHandler(ErrorStatus.CHAT_ROOM_MEMBER_ALREADY_EXISTS);
                });
    }

    private void validateChatRoomPassword(ChatRoom chatRoom, String password) {
        if (chatRoom.getPassword() != null && !chatRoom.getPassword().equals(password)) {
            throw new ChatRoomHandler(ErrorStatus.CHAT_ROOM_ACCESS_DENIED);
        }
    }

    private ChatRoomMember addMemberToChatRoom(Member member, ChatRoom chatRoom) {
        return chatRoomMemberRepository.save(buildChatRoomMember(member, chatRoom));
    }

    private ChatRoomMember buildChatRoomMember(Member member, ChatRoom chatRoom) {
        return ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();
    }

    @Override
    public void leaveChatRoom(Member member, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomHandler(ErrorStatus.CHAT_ROOM_NOT_FOUND));
        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, member.getId())
                .orElseThrow(() -> new ChatRoomHandler(ErrorStatus.CHAT_ROOM_MEMBER_NOT_FOUND));

        validateHostLeaving(member, chatRoom);
        chatRoom.getChatRoomMembers().remove(chatRoomMember);
        checkAndDeleteEmptyChatRoom(chatRoom);
    }

    private void validateHostLeaving(Member member, ChatRoom chatRoom) {
        if (chatRoom.getOwner().equals(member) && chatRoom.getChatRoomMembers().size() > 1) {
            throw new ChatRoomHandler(ErrorStatus.CHAT_ROOM_LEAVE_DENIED);
        }
    }

    private void checkAndDeleteEmptyChatRoom(ChatRoom chatRoom) {
        if (chatRoom.getChatRoomMembers().isEmpty()) {
            chatRoomRepository.delete(chatRoom);
        }
    }

    @Override
    public void deleteChatRoom(Member member, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomHandler(ErrorStatus.CHAT_ROOM_NOT_FOUND));
        checkChatRoomAccess(member, chatRoom);
        chatRoomRepository.delete(chatRoom);
    }

    @Override
    public Long updateChatRoom(Member member, Long chatRoomId, ChatRoomRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomHandler(ErrorStatus.CHAT_ROOM_NOT_FOUND));
        checkChatRoomAccess(member, chatRoom);
        chatRoom.updateChatRoom(request.getName(), request.getDescription(), request.getPassword());
        return chatRoom.getId();
    }

    @Override
    public Long updateLastAccessTime(Member member, Long chatRoomId, LocalDateTime lastAccessTime) {
        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, member.getId())
                .orElseThrow(() -> new ChatRoomHandler(ErrorStatus.CHAT_ROOM_MEMBER_NOT_FOUND));
        chatRoomMember.updateLastAccessTime(LocalDateTime.now());
        return chatRoomMember.getId();
    }

    private void checkChatRoomAccess(Member member, ChatRoom chatRoom) {
        if (!chatRoom.getOwner().equals(member)) {
            throw new ChatRoomHandler(ErrorStatus.CHAT_ROOM_ACCESS_DENIED);
        }
    }
}
