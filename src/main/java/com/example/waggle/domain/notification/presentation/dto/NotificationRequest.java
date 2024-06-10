package com.example.waggle.domain.notification.presentation.dto;

import com.example.waggle.domain.board.persistence.entity.BoardType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class NotificationRequest {
    @Data
    @Builder
    @Schema
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParticipationDto {
        private Long teamId;
        private String teamName;
    }

    @Data
    @Builder
    @Schema
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentDto {
        private BoardType boardType;
        private String content;
    }

    @Data
    @Builder
    @Schema
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MentionDto {
        private String content;
    }
}
