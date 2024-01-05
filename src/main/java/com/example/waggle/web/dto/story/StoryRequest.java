package com.example.waggle.web.dto.story;

import com.example.waggle.global.validation.UpdateCheck;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
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

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString(of = {"id", "content", "username"})
    public static class Put {
        @NotBlank(groups = UpdateCheck.class)
        private Long id;
        @Max(500)
        private String content;
        private String username;

        @Builder.Default
        private List<String> hashtags = new ArrayList<>();
        @Builder.Default
        private List<String> medias = new ArrayList<>();
    }
}
