package com.example.waggle.web.dto.siren;

import com.example.waggle.domain.board.siren.entity.Siren.Category;
import com.example.waggle.domain.member.entity.Gender;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SirenRequest {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Post {

        private String title;
        private String petKind;
        private int petAge;
        private Gender petGender;
        private String contact;
        private String lostLocate;
        private LocalDateTime lostDate;
        private String content;
        private Category category;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Put {
        private Long id;
        private String title;
        private String petKind;
        private int petAge;
        private Gender petGender;
        private String contact;
        private String lostLocate;
        private LocalDateTime lostDate;
        private String content;
        private String username;
        private Category category;

        @Builder.Default
        private List<String> medias = new ArrayList<>();
    }
}
