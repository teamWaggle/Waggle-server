package com.example.waggle.web.dto.story;

import com.example.waggle.web.dto.member.MemberResponse;
import lombok.*;

import java.time.LocalDateTime;
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
        private String profileImg;
        private LocalDateTime createdDate;
        private int recommendCount;
        private Boolean isRecommend;

        @Builder.Default
        private List<String> hashtags = new ArrayList<>();
        @Builder.Default
        private List<String> medias = new ArrayList<>();

        private MemberResponse.SummaryDto member;
        private Boolean isMine;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SummaryDto {

        private Long id;
        private String thumbnail;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ListDto {
        @Builder.Default
        private List<StoryResponse.SummaryDto> storyList = new ArrayList<>();
        private long totalStories;
        private Boolean isFirst;
        private Boolean isLast;

    }
}
