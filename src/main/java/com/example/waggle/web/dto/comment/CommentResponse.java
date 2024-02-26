package com.example.waggle.web.dto.comment;

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
        private List<String> mentionedMemberList;
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
