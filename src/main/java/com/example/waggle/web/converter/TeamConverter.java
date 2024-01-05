package com.example.waggle.web.converter;

import com.example.waggle.domain.schedule.entity.Team;
import com.example.waggle.web.dto.schedule.TeamResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class TeamConverter {

    public static TeamResponse.DetailDto toDetailDto(Team team) {
        return TeamResponse.DetailDto.builder()
                .teamId(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .coverImageUrl(team.getCoverImageUrl())
                .colorScheme(team.getColorScheme())
                .maxTeamSize(team.getMaxTeamSize())
                .leaderUsername(team.getLeader().getUsername())
                .build();
    }

    public static TeamResponse.SummaryDto toSummaryDto(Team team) {
        return TeamResponse.SummaryDto.builder()
                .name(team.getName())
                .description(team.getDescription())
                .coverImageUrl(team.getCoverImageUrl())
                .colorScheme(team.getColorScheme())
                .maxTeamSize(team.getMaxTeamSize())
                .build();
    }


    public static TeamResponse.ListDto toSummaryListDto(Page<Team> teamPage) {
        List<TeamResponse.SummaryDto> teamSummaryDtos = teamPage.stream()
                .map(TeamConverter::toSummaryDto)
                .collect(Collectors.toList());


        return TeamResponse.ListDto.builder()
                .teams(teamSummaryDtos)
                .totalQuestions(teamPage.getTotalElements())
                .isFirst(teamPage.isFirst())
                .isLast(teamPage.isLast())
                .build();
    }
}
