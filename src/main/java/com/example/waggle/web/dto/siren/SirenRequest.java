package com.example.waggle.web.dto.siren;

import com.example.waggle.domain.board.ResolutionStatus;
import com.example.waggle.domain.board.siren.entity.SirenCategory;
import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.global.annotation.valid.ValidEnum;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

        @ValidEnum(target = SirenCategory.class)
        private String category;

        @ValidEnum(target = ResolutionStatus.class)
        private String status;
    }
}
