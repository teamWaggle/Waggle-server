package com.example.waggle.web.dto.comment;

import com.example.waggle.web.dto.member.MemberResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentResponse {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ViewDto {
        private Long id;
        private String content;
        private LocalDateTime createDate;
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
        private List<CommentResponse.ViewDto> commentList = new ArrayList<>();
        private long totalComments;
        private boolean isFirst;
        private boolean isLast;
    }

}
