package com.example.waggle.domain.notification.presentation.converter;

import com.example.waggle.domain.member.presentation.converter.MemberConverter;
import com.example.waggle.domain.notification.persistence.entity.Notification;
import com.example.waggle.domain.notification.presentation.dto.NotificationResponse.NotificationCountDto;
import com.example.waggle.domain.notification.presentation.dto.NotificationResponse.NotificationListDto;
import com.example.waggle.domain.notification.presentation.dto.NotificationResponse.NotificationViewDto;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

public class NotificationConverter {
    public static NotificationViewDto toViewDto(Notification notification) {
        return NotificationViewDto.builder()
                .notificationId(notification.getId())
                .notificationType(notification.getType())
                .receiverId(notification.getReceiverId())
                .isRead(notification.isRead())
                .targetId(notification.getTargetId())
                .sender(MemberConverter.toMemberSummaryDto(notification.getSender()))
                .build();
    }

    public static NotificationListDto toListDto(Page<Notification> pagedNotifications) {
        return NotificationListDto.builder()
                .notificationList(
                        pagedNotifications.getContent().stream()
                                .map(NotificationConverter::toViewDto)
                                .collect(Collectors.toList())
                )
                .build();
    }

    public static NotificationCountDto toCountDto(int count) {
        return NotificationCountDto.builder()
                .count(count)
                .build();
    }
}
