package com.example.waggle.web.dto.answer;

import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        private int recommendCount;
        private Boolean isRecommend;
        private Boolean isOwner;
        private List<String> hashtagList;
        private List<String> mediaList;
        private MemberSummaryDto member;
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
