package com.example.waggle.web.dto.chat;

import com.example.waggle.domain.chat.entity.ChatMessage.MessageType;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryListDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        private MessageType messageType;
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
