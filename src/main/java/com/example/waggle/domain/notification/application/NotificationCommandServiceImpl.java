package com.example.waggle.domain.notification.application;

import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.notification.persistence.dao.NotificationRepository;
import com.example.waggle.domain.notification.persistence.entity.Notification;
import com.example.waggle.domain.notification.persistence.entity.NotificationType;
import com.example.waggle.exception.object.handler.NotificationHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class NotificationCommandServiceImpl implements NotificationCommandService {
    private final NotificationRepository notificationRepository;

    @Override
    public void markNotificationAsRead(Member receiver, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationHandler(ErrorStatus.NOTIFICATION_NOT_FOUND));
        notification.readNotification();
    }

    @Override
    public void convertIsRead(Member receiver, Long notificationId, NotificationType type) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationHandler(ErrorStatus.NOTIFICATION_NOT_FOUND));
        notification.readNotification();
    }
}
