package com.example.waggle.web.dto.schedule;

import com.example.waggle.domain.schedule.entity.TeamColor;
import com.example.waggle.global.annotation.color.ValidTeamColor;
import jakarta.validation.constraints.Min;
import lombok.*;

public class TeamRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Post {
        private String name;
        private String description;
        @ValidTeamColor
        private TeamColor teamColor;
        @Min(value = 1, message = "팀인원수는 최소 1명입니다.(팀 리더 포함 인원 수)")
        private Integer maxTeamSize;
        private String coverImageUrl;
    }

}
