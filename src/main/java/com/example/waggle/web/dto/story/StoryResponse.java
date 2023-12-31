package com.example.waggle.web.dto.story;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class StoryResponse {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class DetailDto {

        private Long id;
        private String content;
        private String username;
        private String profileImg;
        private String createdDate;
        private int recommendCount;
        private boolean recommendIt;

        @Builder.Default
        private List<String> hashtags = new ArrayList<>();
        @Builder.Default
        private List<String> medias = new ArrayList<>();

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SummaryDto {

        private Long id;
        private String username;
        private String profileImg;
        private String createdDate;
        private String thumbnail;
        private int recommendCount;
        private boolean recommendIt;
        @Builder.Default
        private List<String> hashtags = new ArrayList<>();

    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ListDto {
        @Builder.Default
        private List<StoryResponse.SummaryDto> storyList = new ArrayList<>();
        private long totalQuestions;
        private boolean isFirst;
        private boolean isLast;

    }
}
