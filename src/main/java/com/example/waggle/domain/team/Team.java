package com.example.waggle.domain.team;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Team {
    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<TeamMember> teamMembers = new ArrayList<>();

    @Builder
    public Team(Long id, String name, List<Schedule> schedules, List<TeamMember> teamMembers) {
        this.id = id;
        this.name = name;
        if (schedules != null) this.schedules = schedules;
        if (teamMembers != null) this.teamMembers = teamMembers;
    }
}
