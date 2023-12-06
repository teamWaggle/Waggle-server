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
    public static class ViewDto {
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
        private List<CommentResponse.ViewDto> commentList = new ArrayList<>();
        private long totalComments;
        private boolean isFirst;
        private boolean isLast;
    }

}
