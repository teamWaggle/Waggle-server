package com.example.waggle.web.dto.member;

import lombok.*;

import java.time.LocalDateTime;

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
        private LocalDateTime birthday;
        private String profileImgUrl;
    }
}
