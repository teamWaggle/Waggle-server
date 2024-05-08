package com.example.waggle.domain.notification.application;

import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.notification.persistence.entity.Notification;
import com.example.waggle.domain.notification.persistence.dao.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationQueryServiceImpl implements NotificationQueryService {
    private final NotificationRepository notificationRepository;

    @Override
    public Page<Notification> getNotificationList(Member member, Pageable pageable) {
        return notificationRepository.findAllByReceiverId(member.getId(), pageable);
    }

    @Override
    public int countNotReadNotification(Member member) {
        return notificationRepository.countByReceiverIdAndIsRead(member.getId(), false);
    }
}
