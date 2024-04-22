package com.example.waggle.domain.notification.entity;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.component.auditing.BaseEntity;
import com.example.waggle.web.dto.notification.NotificationRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private boolean isRead;

    private Long targetId;      //comment, team, follow

    private Long receiverId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;


    public static Notification of(Member sender, NotificationRequest notificationRequest) {
        return Notification.builder()
                .sender(sender)
                .receiverId(notificationRequest.getReceiverId())
                .targetId(notificationRequest.getTargetId())
                .type(notificationRequest.getType())
                .isRead(false)
                .build();
    }

    public void readNotification() {
        if (!this.isRead) {
            this.isRead = true;
        }
    }
}
