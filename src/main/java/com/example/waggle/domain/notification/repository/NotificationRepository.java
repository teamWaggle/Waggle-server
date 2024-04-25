package com.example.waggle.domain.notification.repository;

import com.example.waggle.domain.notification.entity.Notification;
import com.example.waggle.domain.notification.entity.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByReceiverId(Long receiverId, Pageable pageable);

    Optional<Notification> findByReceiverIdAndTargetIdAndType(Long receiverId, Long targetId, NotificationType type);   //TODO

    int countByReceiverIdAndIsRead(Long receiverId, boolean isRead);    //TODO QUERYDSL
}
