package com.example.waggle.web.dto.question;

import com.example.waggle.domain.board.ResolutionStatus;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QuestionSummaryDto {
        private Long boardId;
        private String title;
        private ResolutionStatus status;
        private LocalDateTime createdDate;
        private List<String> hashtagList;
        private MemberSummaryDto member;
        private int recommendCount;
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
        private String title;
        private ResolutionStatus status;
        private String content;
        private LocalDateTime createdDate;
        private List<String> mediaList;
        private List<String> hashtagList;
        private MemberSummaryDto member;
        private int recommendCount;
        private int viewCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class RepresentativeQuestionDto {
        private List<QuestionSummaryDto> questionList;
    }

}