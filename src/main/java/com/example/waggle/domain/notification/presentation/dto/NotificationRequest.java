package com.example.waggle.domain.notification.presentation.dto;

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
    public static class ParticipationRequestDto {
        private Long teamId;
        private String teamName;
    }
}
