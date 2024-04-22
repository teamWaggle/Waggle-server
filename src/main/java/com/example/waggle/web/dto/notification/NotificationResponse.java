package com.example.waggle.web.dto.notification;

import com.example.waggle.domain.notification.entity.NotificationType;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
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
    public class NotificationViewDto {
        private Long notificationId;
        private Long targetId;
        private Long receiverId;
        private MemberSummaryDto sender;
        private NotificationType notificationType;
    }

    @Data
    @Builder
    @Schema
    @NoArgsConstructor
    @AllArgsConstructor
    public class NotificationListDto {
        private List<NotificationViewDto> notificationList;
    }

    @Data
    @Builder
    @Schema
    @NoArgsConstructor
    @AllArgsConstructor
    public class NotificationCountDto {
        private int count;
    }


}
