package com.example.waggle.domain.team;

import com.example.waggle.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@ToString
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


    @Builder
    public TeamMember(Long id, Team team, Member member) {
        this.id = id;
        this.team = team;
        this.member = member;
    }
    // Team과의 연관관계 편의 메서드
    public void setTeam(Team team) {
        this.team = team;
        team.getTeamMembers().add(this);
    }

    public void removeTeam() {
        if (team != null) {
            team.getTeamMembers().remove(this);
            team = null;
        }
    }

    // Member와의 연관관계 편의 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getTeamMembers().add(this);
    }

    public void removeMember() {
        if (member != null) {
            member.getTeamMembers().remove(this);
            member = null;
        }
    }

}
