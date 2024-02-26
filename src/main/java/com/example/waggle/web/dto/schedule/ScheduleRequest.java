package com.example.waggle.web.dto.schedule;

import lombok.*;

import java.time.LocalDateTime;

public class ScheduleRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleCreateDto {
        private String title;
        private String content;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }

}
