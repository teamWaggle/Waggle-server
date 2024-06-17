package com.example.waggle.domain.schedule.presentation.dto.team;

import com.example.waggle.domain.schedule.persistence.entity.TeamColor;
import com.example.waggle.global.annotation.validation.enums.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotEmpty
    @Size(max = 20, message = "이름은 최대 20자까지 작성할 수 있습니다.")
    private String name;

    @NotEmpty
    @Size(max = 30, message = "설명은 최대 30자까지 작성할 수 있습니다.")
    private String description;

    private String coverImageUrl;

    @NotNull(message = "비공개 여부를 설정해주세요.")
    private Boolean isPrivate;

    @ValidEnum(target = TeamColor.class, message = "유효한 팀 색상을 선택해주세요.")
    private String teamColor;

}
