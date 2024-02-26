package com.example.waggle.web.dto.schedule;

import com.example.waggle.domain.schedule.entity.TeamColor;
import com.example.waggle.global.annotation.valid.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TeamRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class TeamCreateDto {
        private String name;
        private String description;
        @Min(value = 1, message = "팀인원수는 최소 1명입니다.(팀 리더 포함 인원 수)")
        private Integer maxTeamSize;
        private String coverImageUrl;

        @ValidEnum(target = TeamColor.class)
        private String teamColor;
    }

}
