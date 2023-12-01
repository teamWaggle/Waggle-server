package com.example.waggle.domain.member.domain;


import com.example.waggle.global.component.auditing.BaseEntity;
import com.example.waggle.domain.schedule.domain.Schedule;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ScheduleMember extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "schedule_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 연관관계 편의 메서드
    public void addScheduleMember(Schedule schedule, Member member) {
        this.schedule = schedule;
        this.member = member;
        schedule.getScheduleMembers().add(this);
    }

    public void removeTeam() {
        if (schedule != null) {
            schedule.getScheduleMembers().remove(this);
            schedule = null;
            member = null;
        }
    }
}
