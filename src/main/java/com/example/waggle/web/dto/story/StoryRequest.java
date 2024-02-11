package com.example.waggle.web.dto.story;

import jakarta.validation.constraints.Max;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class StoryRequest {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString(of = {"content"})
    public static class Post {
        @Max(500)
        private String content;
        @Builder.Default
        private List<String> hashtags = new ArrayList<>();
        @Builder.Default
        private List<String> medias = new ArrayList<>();
    }
}
