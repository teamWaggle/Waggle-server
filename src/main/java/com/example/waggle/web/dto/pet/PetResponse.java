package com.example.waggle.web.dto.pet;

import com.example.waggle.domain.member.entity.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class PetResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PetDetailDto {
        private Long petId;
        @NotNull
        private String name;
        private String breed;
        private Gender gender;
        private String age;
        private String profileImgUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PetSummaryDto {
        private Long petId;
        @NotNull
        private String name;
        private Gender gender;
        private String profileImgUrl;
    }

}
