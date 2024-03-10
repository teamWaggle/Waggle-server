package com.example.waggle.web.dto.answer;

import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import com.example.waggle.web.dto.recommend.RecommendResponse.RecommendationInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class AnswerResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AnswerViewDto {
        private Long boardId;
        private String content;
        private LocalDateTime createdDate;
        private List<String> hashtagList;
        private List<String> mediaList;
        private MemberSummaryDto member;
        private RecommendationInfo recommendationInfo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AnswerListDto {
        private List<AnswerViewDto> answerList;
        private long answerCount;
        private Boolean isFirst;
        private Boolean isLast;
    }

}
