package com.example.waggle.web.dto.recommend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RecommendResponse {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class RecommendationInfo {
        @Builder.Default
        private Boolean isRecommend = false;
        private int recommendCount;
    }
}
