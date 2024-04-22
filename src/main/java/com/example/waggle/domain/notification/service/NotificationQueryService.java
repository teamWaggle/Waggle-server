package com.example.waggle.domain.notification.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationQueryService {

    Page<Notification> getNotificationList(Member member, Pageable pageable);

    int countNotReadNotification(Member member);
}
