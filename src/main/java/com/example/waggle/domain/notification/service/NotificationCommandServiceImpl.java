package com.example.waggle.domain.notification.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.notification.entity.Notification;
import com.example.waggle.domain.notification.repository.NotificationRepository;
import com.example.waggle.web.dto.notification.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class NotificationCommandServiceImpl implements NotificationCommandService {
    private final NotificationRepository notificationRepository;

    @Override
    public Long sendNotification(Member sender, NotificationRequest notificationRequest) {
        Notification notification = notificationRepository.save(Notification.of(sender, notificationRequest));
        return notification.getId();
    }
}
