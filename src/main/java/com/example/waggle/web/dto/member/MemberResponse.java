package com.example.waggle.web.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MemberResponse {

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

}
