package com.example.waggle.web.converter;

import com.example.waggle.domain.follow.entity.Follow;
import com.example.waggle.domain.notification.entity.NotificationType;
import com.example.waggle.web.dto.notification.NotificationRequest;

public class NotificationConverter {
    public static NotificationRequest toRequest(Follow follow, Long receiverId) {
        return NotificationRequest.builder()
                .type(NotificationType.FOLLOWED)
                .targetId(follow.getId())
                .receiverId(receiverId)
                .build();
    }
}
