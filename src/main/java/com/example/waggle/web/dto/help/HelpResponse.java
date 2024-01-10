package com.example.waggle.web.dto.help;

import com.example.waggle.domain.board.help.entity.Help.Category;
import com.example.waggle.domain.member.entity.Gender;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HelpResponse {
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
        private List<HelpResponse.SummaryDto> helpList = new ArrayList<>();
        private long totalHelps;
        private boolean isFirst;
        private boolean isLast;
    }
}
