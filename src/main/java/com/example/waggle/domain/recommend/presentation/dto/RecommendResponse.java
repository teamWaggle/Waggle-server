package com.example.waggle.domain.recommend.presentation.dto;

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
        private Boolean isRecommend;
        private int recommendCount;
    }
}
