package com.example.waggle.dto.member;

import com.example.waggle.domain.team.Schedule;
import com.example.waggle.domain.team.Team;
import com.example.waggle.domain.team.TeamMember;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class TeamDto {
    private Long id;
    private String name;
    private List<Schedule> schedules = new ArrayList<>();
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


    @Builder
    public TeamDto(Long id, String name, List<Schedule> schedules, List<TeamMember> teamMembers) {
        this.id = id;
        this.name = name;
        if (schedules != null) this.schedules = schedules;
        if (teamMembers != null) this.teamMembers = teamMembers;
    }
}
