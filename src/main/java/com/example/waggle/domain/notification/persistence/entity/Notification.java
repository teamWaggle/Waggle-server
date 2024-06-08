package com.example.waggle.domain.notification.persistence.entity;

import com.example.waggle.domain.auditing.persistence.entity.BaseEntity;
import com.example.waggle.domain.board.persistence.entity.BoardType;
import com.example.waggle.domain.conversation.persistence.entity.Comment;
import com.example.waggle.domain.follow.persistence.entity.Follow;
import com.example.waggle.domain.member.persistence.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.example.waggle.domain.notification.presentation.dto.NotificationRequest.ParticipationRequestDto;

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
                                  ParticipationRequestDto notificationRequest) {
        return Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .type(notificationType)
                .content(String.valueOf(notificationRequest))
                .isRead(false)
                .build();
    }

    public static Notification of(Member member, Follow follow) {
        return Notification.builder()
                .sender(member)
                .type(NotificationType.FOLLOWED)
                .receiver(follow.getToMember())
                .isRead(false)
                .build();
    }

    public static Notification of(Member member, Comment comment, BoardType boardType) {
        return Notification.builder()
                .sender(member)
                .type(NotificationType.COMMENT)
                .receiver(comment.getBoard().getMember())
                .content(String.valueOf(boardType))
                .isRead(false)
                .build();
    }

    public static Notification of(Member sender, Member receiver, String content) {
        return Notification.builder()
                .sender(sender)
                .type(NotificationType.MENTIONED)
                .receiver(receiver)
                .content(content)
                .isRead(false)
                .build();
    }


    public void readNotification() {
        if (!this.isRead) {
            this.isRead = true;
        }
    }
}
