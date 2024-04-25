package com.example.waggle.domain.notification.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.notification.entity.Notification;
import com.example.waggle.domain.notification.entity.NotificationType;
import com.example.waggle.domain.notification.repository.NotificationRepository;
import com.example.waggle.global.exception.handler.NotificationHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
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
        validateNotification(receiver, notification);
        notification.readNotification();
    }

    @Override
    public void convertIsRead(Member receiver, Long targetId, NotificationType type) {
        Notification notification = notificationRepository.findByReceiverIdAndTargetIdAndType(receiver.getId(), targetId, type)
                .orElseThrow(() -> new NotificationHandler(ErrorStatus.NOTIFICATION_NOT_FOUND));
        validateNotification(receiver, notification);
        notification.readNotification();
    }

    private static void validateNotification(Member receiver, Notification notification) {
        if (notification.getReceiverId() != receiver.getId()) {
            throw new NotificationHandler(ErrorStatus.NOTIFICATION_NOT_YOURS);
        }
    }
}
