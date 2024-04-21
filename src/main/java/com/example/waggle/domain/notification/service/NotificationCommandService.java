package com.example.waggle.domain.notification.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.notification.NotificationRequest;

public interface NotificationCommandService {
    Long sendNotification(Member sender, NotificationRequest notificationRequest);
}
