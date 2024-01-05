package com.example.waggle.web.dto.answer;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class AnswerRequest {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Post {
        @NotNull(message = "답변 내용을 작성해주세요.")
        @Max(1500)
        private String content;

        @Builder.Default
        private List<String> hashtags = new ArrayList<>();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Put {
        private Long id;

        @NotNull(message = "답변 내용을 작성해주세요.")
        @Max(1500)
        private String content;
        @Builder.Default
        private List<String> medias = new ArrayList<>();
        @Builder.Default
        private List<String> hashtags = new ArrayList<>();

        private String username;
        private String profileImg;
    }
}
