package com.example.waggle.web.dto.pet;

import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.global.annotation.valid.ValidEnum;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PetRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PetCreateDto {
        @NotNull
        private String name;
        private String breed;
        private String age;
        private String profileImgUrl;
        private boolean isUploadProfile;

        @ValidEnum(target = Gender.class)
        private String gender;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PetListCreateDto {
        private List<PetCreateDto> petList;
    }

}
