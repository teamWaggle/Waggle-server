package com.example.waggle.web.dto.reply;

import com.example.waggle.web.dto.member.MemberResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReplyResponse {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ViewDto {
        private Long id;
        private String content;
        private LocalDateTime createdDate;
        private boolean isMine;
        @Builder.Default
        private List<String> mentionedNickname = new ArrayList<>();
        private MemberResponse.SummaryDto member;
    }

    @Builder
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListDto {
        @Builder.Default
        private List<ReplyResponse.ViewDto> replyList = new ArrayList<>();
        private long totalReplies;
        private boolean isFirst;
        private boolean isLast;
    }
}