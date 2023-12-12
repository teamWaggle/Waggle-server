package com.example.waggle.domain.schedule.domain;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.component.auditing.BaseEntity;
import com.example.waggle.web.dto.schedule.TeamRequest.Post;
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
@EqualsAndHashCode(of = "id",callSuper = false)
public class Team extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String name;
    private String description;
    private String coverImageUrl;
    private String colorScheme;
    private Integer maxTeamSize;


    @Builder.Default
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public void update(Post request) {
        this.name = request.getName();
        this.description = request.getDescription();
//        this.coverImageUrl = request.get
        this.colorScheme = request.getColorScheme();
        this.maxTeamSize = request.getMaxTeamSize();
    }

    public void updateLeader(Member teamLeader) {
        this.leader = teamLeader;
    }

}
