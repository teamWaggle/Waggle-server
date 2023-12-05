package com.example.waggle.web.dto.story;

import com.example.waggle.global.component.file.UploadFile;
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
    public class DetailDto {

        private Long id;
        private String content;
        private String username;
        private UploadFile profileImg;
        private String thumbnail;
        private String createdDate;
        private int recommendCount;
        private boolean recommendIt;

        @Builder.Default
        private List<String> hashtags = new ArrayList<>();
//        @Builder.Default
//        private List<String> medias = new ArrayList<>();
//        @Builder.Default
//        private List<CommentViewDto> comments = new ArrayList<>();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class SummaryDto {

        private Long id;
        private String username;
        private UploadFile profileImg;
        private String thumbnail;
        private String createdDate;
        private int recommendCount;
        private boolean recommendIt;
        @Builder.Default
        private List<String> hashtags = new ArrayList<>();

    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class ListDto {
        @Builder.Default
        private List<StoryResponse.SummaryDto> storyList = new ArrayList<>();
        private long totalQuestions;
        private boolean isFirst;
        private boolean isLast;

    }
}
