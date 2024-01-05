package com.example.waggle.web.dto.question;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuestionResponse {

    @Builder
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SummaryDto {
        private Long id;
        private String title;
        private LocalDateTime createTime;
        private int recommendCount;
        private boolean recommendIt;
        @Builder.Default
        private List<String> hashtags = new ArrayList<>();

        private String username;
        private String profileImg;
    }

    @Builder
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListDto {
        @Builder.Default
        private List<SummaryDto> questionsList = new ArrayList<>();
        private long totalQuestions;
        private boolean isFirst;
        private boolean isLast;
    }

    @Builder
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailDto {
        private Long id;
        private String content;
        private String title;
        private LocalDateTime createDate;
        private int recommendCount;
        private boolean recommendIt;
        @Builder.Default
        private List<String> medias = new ArrayList<>();
        @Builder.Default
        private List<String> hashtags = new ArrayList<>();
        private String username;
        private String profileImg;
    }
}