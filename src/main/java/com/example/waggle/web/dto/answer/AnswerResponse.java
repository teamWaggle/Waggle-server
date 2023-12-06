package com.example.waggle.web.dto.answer;

import com.example.waggle.web.dto.comment.CommentViewDto;
import com.example.waggle.web.dto.reply.ReplyViewDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AnswerResponse {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ViewDto {

        private Long id;
        private String content;
        private String username;
        private LocalDateTime createDate;
        private int recommendCount;
        private boolean recommendIt;

        @Builder.Default
        private List<String> medias = new ArrayList<>();
        @Builder.Default
        private List<CommentViewDto> comments = new ArrayList<>();
        @Builder.Default
        private List<ReplyViewDto> replies = new ArrayList<>();
        @Builder.Default
        private List<String> hashtags = new ArrayList<>();
    }

    @Builder
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListDto {
        @Builder.Default
        private List<AnswerResponse.ViewDto> AnswerList = new ArrayList<>();
        private long totalAnswer;
        private boolean isFirst;
        private boolean isLast;
    }
}
