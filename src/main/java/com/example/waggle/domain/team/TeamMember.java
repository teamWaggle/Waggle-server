package com.example.waggle.domain.team;

import com.example.waggle.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class TeamMember {
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
    public void setTeamMember(Team team, Member member) {
        this.team = team;
        this.member = member;
        team.getTeamMembers().add(this);
    }

    public void removeTeam() {
        if (team != null) {
            team.getTeamMembers().remove(this);
            team = null;
        }
    }

}
