package com.example.waggle.domain.schedule.entity;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.component.auditing.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TeamMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_member_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 연관관계 편의 메서드
    public void addTeamMember(Team team, Member member) {
        team.getTeamMembers().add(this);
        member.getTeamMembers().add(this);
    }

    public void removeTeamMember() {
        if (team != null) {
            team.getTeamMembers().remove(this);
            member.getTeamMembers().remove(this);
            team = null;
            member = null;
        }
    }

}
