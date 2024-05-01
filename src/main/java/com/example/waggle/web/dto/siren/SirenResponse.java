package com.example.waggle.web.dto.siren;

import com.example.waggle.domain.board.ResolutionStatus;
import com.example.waggle.domain.board.siren.entity.SirenCategory;
import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SirenResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class SirenSummaryDto {
        private Long boardId;
        private String title;
        private String thumbnail;
        private LocalDateTime createdDate;
        private String lostLocate;
        private SirenCategory category;
        private ResolutionStatus status;
        private int recommendCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class SirenDetailDto {
        private Long boardId;
        private String title;
        private String petBreed;
        private String petAge;
        private Gender petGender;
        private String contact;
        private LocalDate lostDate;
        private LocalDateTime createdDate;
        private String lostLocate;
        private String content;
        private SirenCategory category;
        private List<String> mediaList;
        private MemberSummaryDto member;
        private ResolutionStatus status;
        private int recommendCount;
        private long viewCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class SirenListDto {
        private List<SirenSummaryDto> sirenList;
        private long sirenCount;
        private Boolean isFirst;
        private Boolean isLast;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class RepresentativeSirenDto {
        private List<SirenSummaryDto> sirenList;
    }

}
