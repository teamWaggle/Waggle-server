package com.example.waggle.web.dto.comment;

import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class CommentViewDto {
        private Long commentId;
        private String content;
        private LocalDateTime createdDate;
        private MemberSummaryDto member;
        private Boolean isOwner;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class CommentListDto {
        private List<CommentViewDto> commentList;
        private long commentCount;
        private Boolean isFirst;
        private Boolean isLast;
    }

}
