package com.example.waggle.web.converter;

import com.example.waggle.domain.chat.entity.ChatRoom;
import com.example.waggle.domain.chat.entity.ChatRoomMember;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.chat.ChatMessageResponse;
import com.example.waggle.web.dto.chat.ChatMessageResponse.ChatMessageDto;
import com.example.waggle.web.dto.chat.ChatMessageResponse.ChatMessageListDto;
import com.example.waggle.web.dto.chat.ChatRoomResponse;
import com.example.waggle.web.dto.chat.ChatRoomResponse.ActiveChatRoomDto;
import com.example.waggle.web.dto.chat.ChatRoomResponse.ActiveChatRoomListDto;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryListDto;
import java.util.List;
import java.util.stream.Collectors;

public class ChatRoomConverter {

    public static ChatRoomResponse.ChatRoomListDto toChatRoomListDto(List<ChatRoom> chatRooms) {
        List<ChatRoomResponse.ChatRoomDetailDto> chatRoomDetailDtos = chatRooms.stream()
                .map(ChatRoomConverter::toChatRoomDetailDto)
                .collect(Collectors.toList());

        return ChatRoomResponse.ChatRoomListDto.builder()
                .chatRooms(chatRoomDetailDtos)
                .build();
    }

    public static ChatRoomResponse.ChatRoomDetailDto toChatRoomDetailDto(ChatRoom chatRoom) {
        List<Member> members = chatRoom.getChatRoomMembers()
                .stream()
                .map(ChatRoomMember::getMember)
                .collect(Collectors.toList());
        MemberSummaryListDto chatRoomMembers = MemberConverter.toMemberListDto(members);

        return ChatRoomResponse.ChatRoomDetailDto.builder()
                .id(chatRoom.getId())
                .name(chatRoom.getName())
                .description(chatRoom.getDescription())
                .chatRoomMembers(chatRoomMembers)
                .owner(MemberConverter.toMemberSummaryDto(chatRoom.getOwner()))
                .build();
    }

    public static ActiveChatRoomListDto toActiveChatRoomList(List<ActiveChatRoomDto> chatRooms) {
        return ChatRoomResponse.ActiveChatRoomListDto.builder()
                .chatRooms(chatRooms)
                .build();
    }

    public static ChatMessageListDto toChatMessageListDto(List<ChatMessageDto> chatMessages) {
        return ChatMessageResponse.ChatMessageListDto.builder()
                .chatMessages(chatMessages)
                .build();
    }
}
