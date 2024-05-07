package com.example.waggle.domain.notification.application;

import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.notification.persistence.entity.NotificationType;

public interface NotificationCommandService {

    void markNotificationAsRead(Member receiver, Long notificationId);

    void convertIsRead(Member receiver, Long targetId, NotificationType type);
}
