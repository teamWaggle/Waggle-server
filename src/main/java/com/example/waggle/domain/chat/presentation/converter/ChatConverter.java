package com.example.waggle.domain.chat.presentation.converter;

import com.example.waggle.domain.chat.persistence.entity.ChatMessage;
import com.example.waggle.domain.chat.persistence.entity.ChatRoom;
import com.example.waggle.domain.chat.persistence.entity.ChatRoomMember;
import com.example.waggle.domain.chat.presentation.dto.ChatResponse.ActiveChatRoomDto;
import com.example.waggle.domain.chat.presentation.dto.ChatResponse.ActiveChatRoomListDto;
import com.example.waggle.domain.chat.presentation.dto.ChatResponse.ChatMessageListDto;
import com.example.waggle.domain.chat.presentation.dto.ChatResponse.ChatRoomDetailDto;
import com.example.waggle.domain.chat.presentation.dto.ChatResponse.ChatRoomJoinDto;
import com.example.waggle.domain.chat.presentation.dto.ChatResponse.ChatRoomListDto;
import com.example.waggle.domain.chat.presentation.dto.ChatResponse.ChatRoomSummaryDto;
import com.example.waggle.domain.chat.presentation.dto.ChatMessageDto;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.member.presentation.converter.MemberConverter;
import com.example.waggle.domain.member.presentation.dto.MemberResponse.MemberSummaryListDto;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.global.util.PageUtil;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public class ChatConverter {

    public static ChatRoomListDto toChatRoomListDto(Page<ChatRoom> chatRooms) {
        List<ChatRoomSummaryDto> chatRoomSummaryDtos = chatRooms.stream()
                .map(ChatConverter::toChatRoomSummaryDto)
                .collect(Collectors.toList());

        return ChatRoomListDto.builder()
                .chatRooms(chatRoomSummaryDtos)
                .nextPageParam(PageUtil.countNextPage(chatRooms))
                .build();
    }

    public static ChatRoomSummaryDto toChatRoomSummaryDto(ChatRoom chatRoom) {
        return ChatRoomSummaryDto.builder()
                .id(chatRoom.getId())
                .name(chatRoom.getName())
                .isPrivate(chatRoom.getPassword() != null && !chatRoom.getPassword().isEmpty())
                .description(chatRoom.getDescription())
                .chatRoomMemberCount(chatRoom.getChatRoomMembers().stream().count())
                .build();
    }

    public static ChatRoomDetailDto toChatRoomDetailDto(ChatRoom chatRoom) {
        List<Member> members = chatRoom.getChatRoomMembers()
                .stream()
                .map(ChatRoomMember::getMember)
                .collect(Collectors.toList());
        MemberSummaryListDto chatRoomMembers = MemberConverter.toMemberListDto(members);

        return ChatRoomDetailDto.builder()
                .id(chatRoom.getId())
                .name(chatRoom.getName())
                .isPrivate(chatRoom.getPassword() != null && !chatRoom.getPassword().isEmpty())
                .description(chatRoom.getDescription())
                .password(chatRoom.getPassword())
                .chatRoomMembers(chatRoomMembers)
                .owner(MemberConverter.toMemberSummaryDto(chatRoom.getOwner()))
                .build();
    }


    public static ActiveChatRoomListDto toActiveChatRoomList(
            List<ActiveChatRoomDto> chatRooms, int nextPageParam) {
        return ActiveChatRoomListDto.builder()
                .chatRooms(chatRooms)
                .nextPageParam(nextPageParam)
                .build();
    }

    public static ChatMessageDto toChatMessageDto(ChatMessage chatMessage, Member sender) {
        String nickname = sender != null ? sender.getNickname() : "알 수 없음";
        String profileImg =
                sender != null ? MediaUtil.getProfileImg(sender) : MediaUtil.getDefaultMemberProfileImgUrl();

        return ChatMessageDto.builder()
                .id(chatMessage.getId())
                .chatRoomId(chatMessage.getChatRoomId())
                .chatMessageType(chatMessage.getChatMessageType())
                .content(chatMessage.getContent())
                .senderUserUrl(sender.getUserUrl())
                .senderNickname(nickname)
                .senderProfileImgUrl(profileImg)
                .sendTime(chatMessage.getSendTime())
                .build();
    }

    public static ChatMessageListDto toChatMessageListDto(List<ChatMessageDto> chatMessages,
                                                          int nextPageParam) {
        Collections.reverse(chatMessages);

        return ChatMessageListDto.builder()
                .chatMessages(chatMessages)
                .nextPageParam(nextPageParam)
                .build();
    }

    public static ActiveChatRoomDto toActiveChatRoomDto(ChatRoom chatRoom,
                                                        long unreadCount,
                                                        String lastMessageContent,
                                                        String lastSenderProfileImgUrl) {
        return ActiveChatRoomDto.builder()
                .id(chatRoom.getId())
                .name(chatRoom.getName())
                .isPrivate(chatRoom.getPassword() != null && !chatRoom.getPassword().isEmpty())
                .unreadCount(unreadCount)
                .lastMessageContent(lastMessageContent)
                .lastSenderProfileImgUrl(lastSenderProfileImgUrl)
                .build();
    }

    public static ChatRoomJoinDto toChatRoomJoinDto(boolean hasJoined) {
        return ChatRoomJoinDto.builder()
                .hasJoined(hasJoined)
                .build();
    }
}
