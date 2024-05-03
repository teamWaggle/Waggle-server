package com.example.waggle.web.dto.comment;

import com.example.waggle.domain.board.ResolutionStatus;
import com.example.waggle.domain.board.siren.entity.SirenCategory;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class CommentListDto {
        private List<CommentViewDto> commentList;
        private int nextPageParam;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QuestionCommentViewDto {
        private Long commentId;
        private String content;
        private String questionTitle;
        private ResolutionStatus questionStatus;
        private LocalDateTime createdDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QuestionCommentListDto {
        private List<QuestionCommentViewDto> commentList;
        private int nextPageParam;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class SirenCommentViewDto {
        private Long commentId;
        private String content;
        private String sirenTitle;
        private ResolutionStatus sirenStatus;
        private SirenCategory sirenCategory;
        private LocalDateTime createdDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class SirenCommentListDto {
        private List<SirenCommentViewDto> commentList;
        private int nextPageParam;
    }

}
