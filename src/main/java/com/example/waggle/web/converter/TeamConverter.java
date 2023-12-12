package com.example.waggle.web.converter;

import com.example.waggle.domain.schedule.domain.Team;
import com.example.waggle.web.dto.schedule.TeamResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

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

    public static TeamResponse.SummaryListDto toSummaryListDto(Page<Team> teamPage) {
        List<TeamResponse.SummaryDto> teamSummaryDtos = teamPage.stream()
                .map(TeamConverter::toSummaryDto)
                .collect(Collectors.toList());

        return TeamResponse.SummaryListDto.builder()
                .teams(teamSummaryDtos)
                .totalQuestions(teamPage.getTotalElements())
                .isFirst(teamPage.isFirst())
                .isLast(teamPage.isLast())
                .build();
    }
}
