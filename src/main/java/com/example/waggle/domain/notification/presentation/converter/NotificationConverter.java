package com.example.waggle.domain.notification.presentation.converter;

import com.example.waggle.domain.member.presentation.converter.MemberConverter;
import com.example.waggle.domain.notification.persistence.entity.Notification;
import com.example.waggle.domain.notification.presentation.dto.NotificationResponse.NotificationCountDto;
import com.example.waggle.domain.notification.presentation.dto.NotificationResponse.NotificationDto;
import com.example.waggle.domain.notification.presentation.dto.NotificationResponse.NotificationListDto;
import com.example.waggle.global.util.ObjectUtil;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

public class NotificationConverter {
    public static NotificationDto toNotificationDto(Notification notification) {
        return NotificationDto.builder()
                .notificationId(notification.getId())
                .notificationType(notification.getType())
                .isRead(notification.isRead())
                .content(ObjectUtil.deserializeNotificationContent(notification))
                .sender(MemberConverter.toMemberSummaryDto(notification.getSender()))
                .build();
    }

    public static NotificationListDto toListDto(Page<Notification> pagedNotifications) {
        return NotificationListDto.builder()
                .notificationList(
                        pagedNotifications.getContent().stream()
                                .map(NotificationConverter::toNotificationDto)
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
