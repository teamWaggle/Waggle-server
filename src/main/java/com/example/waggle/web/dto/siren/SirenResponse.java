package com.example.waggle.web.dto.siren;

import com.example.waggle.domain.board.siren.entity.Siren.Category;
import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.web.dto.member.MemberResponse;
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
        private LocalDateTime createdDate;
        private String lostLocate;
        private Boolean isRecommend;
        private int recommendCount;
        private Category category;

        private MemberResponse.SummaryDto member;
        private Boolean isMine;
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
        private String petAge;
        private Gender petGender;
        private String contact;

        private LocalDateTime lostDate;
        private LocalDateTime createdDate;
        private String lostLocate;
        private String content;
        private Category category;
        @Builder.Default
        private List<String> medias = new ArrayList<>();

        private MemberResponse.SummaryDto member;

        private Boolean isMine;
        private Boolean isRecommend;
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
        private Boolean isFirst;
        private Boolean isLast;
    }
}
