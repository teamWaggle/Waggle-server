package com.example.waggle.domain.chat.service;

import com.example.waggle.domain.chat.entity.ChatRoom;
import com.example.waggle.domain.chat.entity.ChatRoomMember;
import com.example.waggle.domain.chat.repository.ChatRoomMemberRepository;
import com.example.waggle.domain.chat.repository.ChatRoomRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.exception.handler.ChatRoomHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.chat.ChatRoomRequest;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        validateChatRoomPassword(chatRoom, password);
        return addMemberToChatRoom(member, chatRoom).getId();
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
