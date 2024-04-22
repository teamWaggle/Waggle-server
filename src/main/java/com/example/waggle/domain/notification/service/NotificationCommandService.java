package com.example.waggle.domain.notification.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.notification.entity.NotificationType;
import com.example.waggle.web.dto.notification.NotificationRequest;

public interface NotificationCommandService {
    Long sendNotification(Member sender, NotificationRequest notificationRequest);

    void convertIsRead(Member receiver, Long notificationId);

    void convertIsRead(Member receiver, Long targetId, NotificationType type);
}
