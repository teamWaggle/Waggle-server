package com.example.waggle.domain.notification.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.notification.entity.NotificationType;

public interface NotificationCommandService {

    void markNotificationAsRead(Member receiver, Long notificationId);

    void convertIsRead(Member receiver, Long targetId, NotificationType type);
}
