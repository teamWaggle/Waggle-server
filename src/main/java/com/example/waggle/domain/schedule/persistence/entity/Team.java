package com.example.waggle.domain.schedule.persistence.entity;

import com.example.waggle.domain.auditing.persistence.entity.BaseEntity;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.schedule.presentation.dto.team.TeamRequest;
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
@EqualsAndHashCode(of = "id", callSuper = false)
public class Team extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    private String name;
    private String description;
    private String coverImageUrl;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TeamColor teamColor;


    @Builder.Default
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<Schedule> schedules = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "team", orphanRemoval = true)
    private List<TeamMember> teamMembers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private Member leader;

    public void addSchedule(Schedule schedule) {
        this.schedules.add(schedule);
        schedule.setTeam(this);
    }

    public void removeSchedule(Schedule schedule) {
        this.schedules.remove(schedule);
        schedule.setTeam(null);
    }

    public void update(TeamRequest updateTeamRequest) {
        this.name = updateTeamRequest.getName();
        this.description = updateTeamRequest.getDescription();
        this.coverImageUrl = updateTeamRequest.getCoverImageUrl();
        this.teamColor = TeamColor.valueOf(updateTeamRequest.getTeamColor());
    }

    public void updateLeader(Member teamLeader) {
        this.leader = teamLeader;
    }

}
