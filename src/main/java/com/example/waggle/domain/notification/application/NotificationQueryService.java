package com.example.waggle.domain.notification.application;

import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.notification.persistence.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationQueryService {

    Page<Notification> getNotificationList(Member member, Pageable pageable);

    int countNotReadNotification(Member member);
}
