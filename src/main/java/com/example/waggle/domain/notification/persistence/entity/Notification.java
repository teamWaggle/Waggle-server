package com.example.waggle.domain.notification.persistence.entity;

import com.example.waggle.domain.auditing.persistence.entity.BaseEntity;
import com.example.waggle.domain.conversation.persistence.entity.Comment;
import com.example.waggle.domain.follow.persistence.entity.Follow;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.schedule.persistence.entity.Participation;
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

    public static Notification of(Member member, Participation participation) {
        return Notification.builder()
                .sender(member)
                .type(NotificationType.PARTICIPATION_REQUEST)
                .targetId(participation.getId())
                .receiverId(participation.getTeam().getLeader().getId())
                .isRead(false)
                .build();
    }

    public static Notification of(Member member, Follow follow) {
        return Notification.builder()
                .sender(member)
                .type(NotificationType.FOLLOWED)
                .targetId(follow.getId())
                .receiverId(follow.getToMember().getId())
                .isRead(false)
                .build();
    }

    public static Notification of(Member member, Comment comment) {
        return Notification.builder()
                .sender(member)
                .type(NotificationType.COMMENT)
                .targetId(comment.getId())
                .receiverId(comment.getBoard().getMember().getId())
                .isRead(false)
                .build();
    }

    public void readNotification() {
        if (!this.isRead) {
            this.isRead = true;
        }
    }
}
