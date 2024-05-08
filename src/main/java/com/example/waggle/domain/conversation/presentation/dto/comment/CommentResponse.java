package com.example.waggle.domain.conversation.presentation.dto.comment;

import com.example.waggle.domain.board.persistence.entity.ResolutionStatus;
import com.example.waggle.domain.board.persistence.entity.SirenCategory;
import com.example.waggle.domain.member.presentation.dto.MemberResponse.MemberSummaryDto;
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
        private String title;
        private ResolutionStatus status;
        private LocalDateTime createdDate;
        private MemberSummaryDto member;
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
        private String title;
        private ResolutionStatus status;
        private SirenCategory category;
        private LocalDateTime createdDate;
        private MemberSummaryDto member;
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
