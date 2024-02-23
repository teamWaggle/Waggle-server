package com.example.waggle.web.dto.siren;

import com.example.waggle.domain.board.ResolutionStatus;
import com.example.waggle.domain.board.siren.entity.SirenCategory;
import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.global.annotation.valid.ValidEnum;
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

        private String contact;
        private String lostLocate;
        private LocalDateTime lostDate;
        private String content;

        @ValidEnum(target = SirenCategory.class)
        private String category;

        @ValidEnum(target = ResolutionStatus.class)
        private String status;

        @ValidEnum(target = Gender.class)
        private String petGender;
    }
}
