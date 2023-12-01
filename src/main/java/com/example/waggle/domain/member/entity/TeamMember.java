package com.example.waggle.domain.member.entity;

import com.example.waggle.global.component.auditing.BaseEntity;
import com.example.waggle.domain.schedule.domain.Team;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class TeamMember extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "team_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 연관관계 편의 메서드
    public void addTeamMember(Team team, Member member) {
        this.team = team;
        this.member = member;
        team.getTeamMembers().add(this);
        member.getTeamMembers().add(this);
    }

    public void removeTeam() {
        if (team != null) {
            team.getTeamMembers().remove(this);
            member.getTeamMembers().remove(this);
            team = null;
            member = null;
        }
    }

}
