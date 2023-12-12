package com.example.waggle.web.dto.reply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class ReplyRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Post {
        private String content;
        @Builder.Default
        private List<String> mentions = new ArrayList<>();
        private Long commentId;
    }
}