package com.example.waggle.domain.schedule.presentation.dto.team;

import com.example.waggle.domain.schedule.persistence.entity.TeamColor;
import com.example.waggle.global.annotation.valid.enums.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class TeamRequest {
    private String name;
    private String description;
    private String coverImageUrl;

    @ValidEnum(target = TeamColor.class)
    private String teamColor;
}
