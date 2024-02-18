package com.example.waggle.web.dto.member;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MemberResponse {

    @Builder
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SummaryDto {
        private Long id;
        private String userUrl;
        private String nickname;
        private String profileImgUrl;
    }

    @Builder
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailDto {
        private Long id;
        private String userUrl;
        private String nickname;
        private String name;
        private LocalDate birthday;
        private String profileImgUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListDto {
        @Builder.Default
        private List<SummaryDto> members = new ArrayList<>();
        private int totalSize;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailListDto {
        @Builder.Default
        private List<String> emails = new ArrayList<>();
    }
}
