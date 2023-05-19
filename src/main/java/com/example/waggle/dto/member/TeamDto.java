package com.example.waggle.dto.member;

import com.example.waggle.domain.team.Schedule;
import com.example.waggle.domain.team.Team;
import com.example.waggle.domain.team.TeamMember;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TeamDto {
    private Long id;
    private String name;
    @Builder.Default
    private List<Schedule> schedules = new ArrayList<>();
    @Builder.Default
    private List<TeamMember> teamMembers = new ArrayList<>();

    static public TeamDto toDto(Team team) {
        return TeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .schedules(team.getSchedules())
                .teamMembers(team.getTeamMembers()).build();
    }

    public Team toEntity() {
        Team.TeamBuilder teamBuilder = Team.builder()
                .id(id)
                .name(name)
                .teamMembers(teamMembers)
                .schedules(schedules);
        return teamBuilder.build();
    }
}
