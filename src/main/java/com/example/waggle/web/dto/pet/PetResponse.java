package com.example.waggle.web.dto.pet;

import com.example.waggle.domain.member.entity.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class PetResponse {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class DetailDto {
        private Long id;
        @NotNull
        private String name;
        private String breed;
        private Gender gender;
        private String description;
        private String age;
        private String profileImgUrl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class SummaryDto {
        private Long id;
        @NotNull
        private String name;
        private Gender gender;
        private String profileImgUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ListDto {
        private List<DetailDto> petList = new ArrayList<>();
    }

}
