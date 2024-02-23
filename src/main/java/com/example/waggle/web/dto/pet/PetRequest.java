package com.example.waggle.web.dto.pet;

import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.global.annotation.valid.ValidEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
        @NotNull
        private String name;
        private String breed;
        private String age;
        private String description;
        private String profileImgUrl;
        @ValidEnum(target = Gender.class)
        private String gender;
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
