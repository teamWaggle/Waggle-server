package com.example.waggle.domain.schedule.entity;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.component.auditing.BaseEntity;
import com.example.waggle.web.dto.oauth.OAuthToken.request;
import com.example.waggle.web.dto.schedule.TeamRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Team extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String name;
    private String description;
    private String coverImageUrl;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TeamColor teamColor;
    private Integer maxTeamSize;


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
        this.maxTeamSize = updateTeamRequest.getMaxTeamSize();
    }

    public void updateLeader(Member teamLeader) {
        this.leader = teamLeader;
    }

}
