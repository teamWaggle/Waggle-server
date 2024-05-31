package com.example.waggle.domain.pet.presentation.dto;

import com.example.waggle.domain.member.persistence.entity.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class PetResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class PetDetailDto {
        private Long petId;
        @NotNull
        private String name;
        private String breed;
        private Gender gender;
        private String description;
        private String age;
        private String profileImgUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class PetSummaryDto {
        private Long petId;
        @NotNull
        private String name;
        private Gender gender;
        private String profileImgUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PetListDto {
        private List<PetDetailDto> petList;
    }

}
