package com.example.waggle.web.dto.schedule;

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
        private Long id;
        private String name;
        private String description;
        private String coverImageUrl;
        private String colorScheme;
        private Integer maxTeamSize;
        // schedules, teamMembers, teamLeader
    }

}
