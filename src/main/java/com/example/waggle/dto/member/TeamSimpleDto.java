package com.example.waggle.dto.member;

import com.example.waggle.domain.team.Team;
import com.example.waggle.domain.team.TeamMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamSimpleDto {
    private Long id;
    private String name;

    static public TeamSimpleDto toDto(TeamMember teamMember) {
        return TeamSimpleDto.builder()
                .id(teamMember.getTeam().getId())
                .name(teamMember.getTeam().getName())
                .build();
    }
}
