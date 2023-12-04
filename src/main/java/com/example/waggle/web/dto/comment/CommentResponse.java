package com.example.waggle.web.dto.comment;

import com.example.waggle.web.dto.reply.ReplyViewDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class CommentResponse {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class viewDto {
        private Long id;
        private String content;
        private String username;
        @Builder.Default
        private List<ReplyViewDto> replies = new ArrayList<>();
    }

    @Builder
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListDto {
        @Builder.Default
        private List<CommentResponse.viewDto> helpList = new ArrayList<>();
        private long totalQuestions;
        private boolean isFirst;
        private boolean isLast;
    }

}
