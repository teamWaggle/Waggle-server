package com.example.waggle.web.converter;

import com.example.waggle.domain.notification.entity.Notification;
import com.example.waggle.web.dto.notification.NotificationResponse.NotificationCountDto;
import com.example.waggle.web.dto.notification.NotificationResponse.NotificationListDto;
import com.example.waggle.web.dto.notification.NotificationResponse.NotificationViewDto;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

public class NotificationConverter {
    public static NotificationViewDto toViewDto(Notification notification) {
        return NotificationViewDto.builder()
                .notificationId(notification.getId())
                .notificationType(notification.getType())
                .receiverId(notification.getReceiverId())
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
