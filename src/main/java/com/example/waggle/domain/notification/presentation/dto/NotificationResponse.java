package com.example.waggle.domain.notification.presentation.dto;

import com.example.waggle.domain.member.presentation.dto.MemberResponse.MemberSummaryDto;
import com.example.waggle.domain.notification.persistence.entity.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class NotificationResponse {
    @Data
    @Builder
    @Schema
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationViewDto {
        private Long notificationId;
        private boolean isRead;
        private String content;
        private MemberSummaryDto sender;
        private NotificationType notificationType;
    }

    @Data
    @Builder
    @Schema
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationListDto {
        private List<NotificationViewDto> notificationList;
    }

    @Data
    @Builder
    @Schema
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationCountDto {
        private int count;
    }


}
