package com.example.waggle.web.dto.pet;

import com.example.waggle.domain.member.entity.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PetRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Post {
        private Long id;
        @NotNull
        private String name;
        private String breed;
        private Gender gender;
        private LocalDate birthday;
        private String profileImgUrl;
        private boolean isUploadProfile;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class PostList {
        @Builder.Default
        private List<Post> petList = new ArrayList<>();
    }
}
