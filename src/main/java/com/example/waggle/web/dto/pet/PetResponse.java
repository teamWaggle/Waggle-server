package com.example.waggle.web.dto.pet;

import com.example.waggle.domain.member.entity.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

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
        private LocalDate birthday;
        private String profileImgUrl;
//        @NotNull
//        private String username;
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

}
