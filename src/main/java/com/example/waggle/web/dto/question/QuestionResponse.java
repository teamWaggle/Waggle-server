package com.example.waggle.web.dto.question;

import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class QuestionResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QuestionSummaryDto {
        private Long boardId;
        private String title;
        private LocalDateTime createdDate;
        private int recommendCount;
        private Boolean isRecommend;
        private List<String> hashtagList;
        private MemberSummaryDto member;
        private Boolean isOwner;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QuestionSummaryListDto {
        private List<QuestionSummaryDto> questionList;
        private long questionCount;
        private Boolean isFirst;
        private Boolean isLast;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QuestionDetailDto {
        private Long boardId;
        private String content;
        private String title;
        private LocalDateTime createdDate;
        private int recommendCount;
        private Boolean isRecommend;
        private Boolean isOwner;
        private List<String> mediaList;
        private List<String> hashtagList;
        private MemberSummaryDto member;
    }

}