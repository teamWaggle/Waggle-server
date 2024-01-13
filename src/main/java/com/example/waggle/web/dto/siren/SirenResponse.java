package com.example.waggle.web.dto.siren;

import com.example.waggle.domain.board.siren.entity.Siren.Category;
import com.example.waggle.domain.member.entity.Gender;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SirenResponse {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class SummaryDto {

        private Long id;
        private String title;
        private String thumbnail;
        private LocalDateTime lostDate;
        private String lostLocate;
        private boolean isRecommend;
        private int recommendCount;
        private Category category;

        private String username;
        private String profileImg;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class DetailDto {
        private Long id;
        private String title;
        private String petKind;
        private int petAge;
        private Gender petGender;
        private String contact;

        private LocalDateTime lostDate;
        private String lostLocate;
        private String content;
        private Category category;
        @Builder.Default
        private List<String> medias = new ArrayList<>();

        private String username;
        private String profileImg;

        private boolean isRecommend;
        private int recommendCount;
    }

    @Builder
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListDto {
        @Builder.Default
        private List<SirenResponse.SummaryDto> sirenList = new ArrayList<>();
        private long totalSirens;
        private boolean isFirst;
        private boolean isLast;
    }
}
