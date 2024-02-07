package com.example.waggle.web.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ScheduleResponse {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class DetailDto {
        private Long scheduleId;
        private Long teamId;
        private String title;
        private String content;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class ListDto {
        @Builder.Default
        private List<ScheduleResponse.DetailDto> scheduleList = new ArrayList<>();
        private long totalStories;
        private boolean isFirst;
        private boolean isLast;
    }

}
