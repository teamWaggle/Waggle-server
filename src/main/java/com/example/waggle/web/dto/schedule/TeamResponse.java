package com.example.waggle.web.dto.schedule;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class TeamResponse {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class DetailDto {
        private Long teamId;
        private String name;
        private String description;
        private String coverImageUrl;
        private String colorScheme;
        private Integer maxTeamSize;
        // schedules, teamMembers, teamLeader
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class ListDto {
        List<DetailDto> teams;
    }
}
