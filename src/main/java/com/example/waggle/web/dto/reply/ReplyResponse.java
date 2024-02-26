package com.example.waggle.web.dto.reply;

import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ReplyResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReplyViewDto {
        private Long replyId;
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
    public static class ReplyListDto {
        private List<ReplyViewDto> replyList;
        private long replyCount;
        private Boolean isFirst;
        private Boolean isLast;
    }

}