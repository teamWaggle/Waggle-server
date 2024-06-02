package com.example.waggle.domain.board.presentation.dto.siren;

import com.example.waggle.domain.board.persistence.entity.ResolutionStatus;
import com.example.waggle.domain.board.persistence.entity.SirenCategory;
import com.example.waggle.domain.member.persistence.entity.Gender;
import com.example.waggle.domain.member.presentation.dto.MemberResponse.MemberSummaryDto;
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
        private int commentCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class SirenPagedSummaryListDto {
        private List<SirenSummaryDto> sirenList;
        private int nextPageParam;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class SirenSummaryListDto {
        private List<SirenSummaryDto> sirenList;
    }

}
