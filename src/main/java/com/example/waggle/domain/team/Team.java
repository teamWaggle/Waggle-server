package com.example.waggle.domain.team;

import com.example.waggle.component.auditing.BaseEntity;
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
}
