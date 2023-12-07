package com.example.waggle.web.converter;

import com.example.waggle.domain.schedule.domain.Team;
import com.example.waggle.web.dto.schedule.TeamResponse;

public class TeamConverter {

    public static TeamResponse.DetailDto toDetailDto(Team team) {
        return TeamResponse.DetailDto.builder()
                .teamId(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .coverImageUrl(team.getCoverImageUrl())
                .colorScheme(team.getColorScheme())
                .maxTeamSize(team.getMaxTeamSize()).build();
        // TODO schedules, teamMembers, teamLeader
    }
}
