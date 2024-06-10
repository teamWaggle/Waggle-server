package com.example.waggle.domain.notification.persistence.entity;

import com.example.waggle.domain.auditing.persistence.entity.BaseEntity;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.global.util.ObjectUtil;
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

    private String content;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    public static Notification of(Member sender,
                                  Member receiver,
                                  NotificationType notificationType,
                                  Object content) {
        return Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .type(notificationType)
                .content(ObjectUtil.serialize(content))
                .isRead(false)
                .build();
    }


    public void readNotification() {
        if (!this.isRead) {
            this.isRead = true;
        }
    }
}
