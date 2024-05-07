package com.example.waggle.domain.member.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class MemberResponse {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class MemberStorageDto {
        private Long memberId;
        private String userUrl;
        private String profileImgUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class MemberSummaryDto {
        private Long memberId;
        private String userUrl;
        private String nickname;
        private String profileImgUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class MemberDetailDto {
        private Long memberId;
        private String userUrl;
        private String nickname;
        private String name;
        private LocalDate birthday;
        private String profileImgUrl;
        private Long followingCount;
        private Long followerCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class MemberSummaryListDto {
        private List<MemberSummaryDto> memberList;
        private int memberCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class EmailListDto {
        private List<String> emailList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class MemberMentionDto {
        private String id;
        private String display;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class MemberMentionListDto {
        private List<MemberMentionDto> mentionList;
    }

}
