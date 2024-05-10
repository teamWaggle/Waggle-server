package com.example.waggle.domain.chat.presentation.dto;

import com.example.waggle.domain.chat.persistence.entity.ChatMessageType;
import com.example.waggle.domain.member.presentation.dto.MemberResponse.MemberSummaryDto;
import com.example.waggle.domain.member.presentation.dto.MemberResponse.MemberSummaryListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ChatResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ChatRoomDetailDto {
        private Long id;
        private String name;
        private String description;
        private String password;
        private MemberSummaryListDto chatRoomMembers;
        private MemberSummaryDto owner;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ChatRoomListDto {
        private List<ChatRoomDetailDto> chatRooms;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ActiveChatRoomDto {
        private Long id;
        private String name;
        private long unreadCount;
        private String lastMessageContent;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ActiveChatRoomListDto {
        private List<ActiveChatRoomDto> chatRooms;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ChatMessageDto {
        private String id;
        private String content;
        private LocalDateTime sendTime;
        private MemberSummaryDto sender;
        private ChatMessageType chatMessageType;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ChatMessageListDto {
        private List<ChatMessageDto> chatMessages;
    }

}
