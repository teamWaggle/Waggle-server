package com.example.waggle.web.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class CommentRequest {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Post {
        private Long id;
        private String content;
        private String username;
        private Long boardId;
        @Builder.Default
        List<String> mentionedNickname = new ArrayList<>();
    }
}
