package com.example.waggle.web.dto.story;

import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class StoryResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoryDetailDto {
        private Long boardId;
        private String content;
        private String profileImg;
        private LocalDateTime createdDate;
        private int recommendCount;
        private Boolean isRecommend;
        private List<String> hashtagList;
        private List<String> mediaList;
        private MemberSummaryDto member;
        private Boolean isOwner;
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
        private long storyCount;
        private Boolean isFirst;
        private Boolean isLast;
    }

}
