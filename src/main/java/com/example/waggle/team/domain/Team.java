package com.example.waggle.team.domain;

import com.example.waggle.commons.component.auditing.BaseEntity;
import com.example.waggle.schedule.domain.Schedule;
import com.example.waggle.member.domain.Member;
import com.example.waggle.member.domain.TeamMember;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Team extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "team")
    private List<Schedule> schedules = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "team")
    private List<TeamMember> teamMembers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "team_leader_id")
    private Member teamLeader;

    public void updateTeamName(String name) {
        this.name = name;
    }

    public void updateTeamLeader(Member teamLeader) {
        this.teamLeader = teamLeader;
    }

}
