package com.example.waggle.web.dto.pet;

import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.global.annotation.valid.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class PetRequest {
    @NotNull
    private String name;
    private String breed;
    private String age;
    private String profileImgUrl;
    private boolean isUploadProfile;

    @ValidEnum(target = Gender.class)
    private String gender;
}
