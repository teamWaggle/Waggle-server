package com.example.waggle.web.dto.chat;

import com.example.waggle.domain.chat.entity.ChatMessage.MessageType;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ChatMessageResponse {

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
