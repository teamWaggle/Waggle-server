package com.example.waggle.web.dto.member;

import lombok.*;

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
        private String username;
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
        private String username;
        private String nickname;
        private String address;
        private String phone;
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

}
