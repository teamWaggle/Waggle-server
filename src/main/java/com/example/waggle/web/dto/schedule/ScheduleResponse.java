package com.example.waggle.web.dto.schedule;

import com.example.waggle.domain.schedule.entity.ScheduleStatus;
import com.example.waggle.domain.schedule.entity.TeamColor;
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
        private TeamColor teamColor;
        private String title;
        private String content;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private ScheduleStatus status;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class ListDto {
        @Builder.Default
        private List<ScheduleResponse.DetailDto> scheduleList = new ArrayList<>();
        private long totalSchedules;
        private boolean isFirst;
        private boolean isLast;
    }

}
