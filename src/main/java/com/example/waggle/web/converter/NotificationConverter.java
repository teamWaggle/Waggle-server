package com.example.waggle.web.converter;

import com.example.waggle.domain.follow.entity.Follow;
import com.example.waggle.domain.notification.entity.Notification;
import com.example.waggle.domain.notification.entity.NotificationType;
import com.example.waggle.domain.schedule.entity.Participation;
import com.example.waggle.web.dto.notification.NotificationRequest;
import com.example.waggle.web.dto.notification.NotificationResponse.NotificationCountDto;
import com.example.waggle.web.dto.notification.NotificationResponse.NotificationListDto;
import com.example.waggle.web.dto.notification.NotificationResponse.NotificationViewDto;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

public class NotificationConverter {
    public static NotificationRequest toRequest(Follow follow, Long receiverId) {
        return NotificationRequest.builder()
                .type(NotificationType.FOLLOWED)
                .targetId(follow.getId())
                .receiverId(receiverId)
                .build();
    }

    public static NotificationRequest toRequest(Participation participation, Long receiverId) {
        return NotificationRequest.builder()
                .type(NotificationType.PARTICIPATION_REQUEST)
                .targetId(participation.getId())
                .receiverId(receiverId)
                .build();
    }

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
