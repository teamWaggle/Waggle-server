package com.example.waggle.domain.notification.persistence.dao;

import com.example.waggle.domain.notification.persistence.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByReceiverId(Long receiverId, Pageable pageable);

    int countByReceiverIdAndIsRead(Long receiverId, boolean isRead);    //TODO QUERYDSL
}
