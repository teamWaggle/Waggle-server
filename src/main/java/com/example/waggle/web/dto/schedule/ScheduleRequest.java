package com.example.waggle.web.dto.schedule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

public class ScheduleRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ScheduleCreateDto {
        private String title;
        private String content;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }

}
