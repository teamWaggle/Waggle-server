package com.example.waggle.domain.notification.entity;

import com.example.waggle.domain.follow.entity.Follow;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.schedule.entity.Participation;
import com.example.waggle.global.component.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.example.waggle.domain.notification.entity.NotificationType.FOLLOWED;
import static com.example.waggle.domain.notification.entity.NotificationType.PARTICIPATION_REQUEST;

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
                .type(PARTICIPATION_REQUEST)
                .targetId(participation.getId())
                .receiverId(participation.getTeam().getLeader().getId())
                .isRead(false)
                .build();
    }

    public static Notification of(Member member, Follow follow) {
        return Notification.builder()
                .sender(member)
                .type(FOLLOWED)
                .targetId(follow.getId())
                .receiverId(follow.getToMember().getId())
                .isRead(false)
                .build();
    }

    public void readNotification() {
        if (!this.isRead) {
            this.isRead = true;
        }
    }
}
