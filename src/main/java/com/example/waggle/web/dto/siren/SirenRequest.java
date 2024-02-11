package com.example.waggle.web.dto.siren;

import com.example.waggle.domain.board.siren.entity.Siren.Category;
import com.example.waggle.domain.member.entity.Gender;
import lombok.*;

import java.time.LocalDateTime;

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
        private String petAge;
        private Gender petGender;
        private String contact;
        private String lostLocate;
        private LocalDateTime lostDate;
        private String content;
        private Category category;
    }
}
