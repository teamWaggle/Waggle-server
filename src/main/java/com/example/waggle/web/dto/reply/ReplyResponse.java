package com.example.waggle.web.dto.reply;

import lombok.*;

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
        private String username;
        @Builder.Default
        private List<String> mentionedUsername = new ArrayList<>();
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