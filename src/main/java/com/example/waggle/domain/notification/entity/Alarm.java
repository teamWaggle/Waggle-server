package com.example.waggle.domain.notification.entity;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.component.auditing.BaseEntity;
import com.example.waggle.web.dto.kafka.AlarmArgs;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlarmType alarmType;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isRead = false;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private AlarmArgs alarmArgs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "called_member_id", nullable = false, updatable = false)
    private Member calledMember;
}
