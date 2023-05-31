package com.example.waggle.dto.member;

import com.example.waggle.domain.team.Schedule;
import com.example.waggle.domain.team.Team;
import com.example.waggle.domain.team.TeamMember;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TeamDto {
    private Long id;
    private String name;
    @Builder.Default
    private List<ScheduleDto> schedules = new ArrayList<>();
    @Builder.Default
    private List<String> teamMembers = new ArrayList<>();

    static public TeamDto toDto(Team team) {
        return TeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .schedules(team.getSchedules().stream().map(ScheduleDto::toDto).collect(Collectors.toList()))
                .teamMembers(team.getTeamMembers().stream()
                        .map(tm -> tm.getMember().getUsername()).collect(Collectors.toList()))
                .build();
    }

    public Team toEntity(List<TeamMember> teamMembers, List<Schedule> schedules) {
        Team.TeamBuilder teamBuilder = Team.builder()
                .id(id)
                .name(name)
                .teamMembers(teamMembers)
                .schedules(schedules);
        return teamBuilder.build();
    }
}
