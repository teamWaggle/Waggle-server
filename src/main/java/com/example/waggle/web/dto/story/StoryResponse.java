package com.example.waggle.web.dto.story;

import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class StoryResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoryDetailDto {
        private Long boardId;
        private String content;
        private LocalDateTime createdDate;
        private List<String> hashtagList;
        private List<String> mediaList;
        private MemberSummaryDto member;
        private int recommendCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StorySummaryDto {
        private Long boardId;
        private String thumbnail;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StorySummaryListDto {
        private List<StorySummaryDto> storyList;
        private int nextPageParam;
    }

}
