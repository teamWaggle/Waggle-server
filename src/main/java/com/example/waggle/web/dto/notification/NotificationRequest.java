package com.example.waggle.web.dto.notification;

import com.example.waggle.domain.notification.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private Long targetId;
    private Long receiverId;
    private NotificationType type;
}
