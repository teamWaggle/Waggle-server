package com.example.waggle.web.dto.pet;

import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.global.annotation.valid.ValidEnum;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
        private String profileImgUrl;
        private boolean isUploadProfile;

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
