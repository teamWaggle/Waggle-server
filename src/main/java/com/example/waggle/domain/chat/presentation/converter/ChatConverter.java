package com.example.waggle.domain.chat.presentation.converter;

import com.example.waggle.domain.chat.persistence.entity.ChatMessage;
import com.example.waggle.domain.chat.persistence.entity.ChatRoom;
import com.example.waggle.domain.chat.persistence.entity.ChatRoomMember;
import com.example.waggle.domain.chat.presentation.dto.ChatResponse;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.member.presentation.converter.MemberConverter;
import com.example.waggle.domain.member.presentation.dto.MemberResponse.MemberSummaryListDto;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class ChatConverter {

    public static ChatResponse.ChatRoomListDto toChatRoomListDto(List<ChatRoom> chatRooms) {
        List<ChatResponse.ChatRoomDetailDto> chatRoomDetailDtos = chatRooms.stream()
                .map(ChatConverter::toChatRoomDetailDto)
                .collect(Collectors.toList());

        return ChatResponse.ChatRoomListDto.builder()
                .chatRooms(chatRoomDetailDtos)
                .build();
    }

    public static ChatResponse.ChatRoomDetailDto toChatRoomDetailDto(ChatRoom chatRoom) {
        List<Member> members = chatRoom.getChatRoomMembers()
                .stream()
                .map(ChatRoomMember::getMember)
                .collect(Collectors.toList());
        MemberSummaryListDto chatRoomMembers = MemberConverter.toMemberListDto(members);

        return ChatResponse.ChatRoomDetailDto.builder()
                .id(chatRoom.getId())
                .name(chatRoom.getName())
                .description(chatRoom.getDescription())
                .password(chatRoom.getPassword())
                .chatRoomMembers(chatRoomMembers)
                .owner(MemberConverter.toMemberSummaryDto(chatRoom.getOwner()))
                .build();
    }


    public static ChatResponse.ActiveChatRoomListDto toActiveChatRoomList(
            List<ChatResponse.ActiveChatRoomDto> chatRooms) {
        return ChatResponse.ActiveChatRoomListDto.builder()
                .chatRooms(chatRooms)
                .build();
    }

    public static ChatResponse.ChatMessageDto toChatMessageDto(ChatMessage chatMessage, Member sender) {
        return ChatResponse.ChatMessageDto.builder()
                .id(chatMessage.getId())
                .content(chatMessage.getContent())
                .sendTime(Instant.ofEpochMilli(chatMessage.getSendTime()).atZone(ZoneId.of("Asia/Seoul"))
                        .toLocalDateTime())
                .chatMessageType(chatMessage.getChatMessageType())
                .sender(MemberConverter.toMemberSummaryDto(sender))
                .build();
    }

    public static ChatResponse.ChatMessageListDto toChatMessageListDto(List<ChatResponse.ChatMessageDto> chatMessages) {
        return ChatResponse.ChatMessageListDto.builder()
                .chatMessages(chatMessages)
                .build();
    }

    public static ChatResponse.ActiveChatRoomDto toActiveChatRoomDto(ChatRoom chatRoom, long unreadCount,
                                                                     String lastMessageContent) {
        return ChatResponse.ActiveChatRoomDto.builder()
                .id(chatRoom.getId())
                .name(chatRoom.getName())
                .unreadCount(unreadCount)
                .lastMessageContent(lastMessageContent)
                .build();
    }
}
