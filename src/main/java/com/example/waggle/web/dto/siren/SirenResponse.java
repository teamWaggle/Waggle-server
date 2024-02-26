package com.example.waggle.web.dto.siren;

import com.example.waggle.domain.board.siren.entity.SirenCategory;
import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SirenResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SirenSummaryDto {
        private Long boardId;
        private String title;
        private String thumbnail;
        private LocalDateTime lostDate;
        private LocalDateTime createdDate;
        private String lostLocate;
        private Boolean isRecommend;
        private int recommendCount;
        private SirenCategory category;
        private MemberSummaryDto member;
        private Boolean isOwner;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SirenDetailDto {
        private Long boardId;
        private String title;
        private String petBreed;
        private String petAge;
        private Gender petGender;
        private String contact;
        private LocalDateTime lostDate;
        private LocalDateTime createdDate;
        private String lostLocate;
        private String content;
        private SirenCategory category;
        private List<String> mediaList;
        private MemberSummaryDto member;
        private Boolean isOwner;
        private Boolean isRecommend;
        private int recommendCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SirenListDto {
        private List<SirenSummaryDto> sirenList;
        private long sirenCount;
        private Boolean isFirst;
        private Boolean isLast;
    }

}
