package com.example.waggle.web.dto.schedule;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.schedule.domain.Team;
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

    private String teamLeader;

    static public TeamDto toDto(Team team) {
        return TeamDto.builder()
                .id(team.getId())
                .name(team.getName())
                .schedules(team.getSchedules().stream().map(ScheduleDto::toDto).collect(Collectors.toList()))
                .teamMembers(team.getTeamMembers().stream()
                        .map(tm -> tm.getMember().getUsername()).collect(Collectors.toList()))
                .teamLeader(team.getTeamLeader().getUsername())
                .build();
    }

    public Team toEntity(Member teamLeader) {
        return Team.builder().name(name).teamLeader(teamLeader).build();
    }
}
