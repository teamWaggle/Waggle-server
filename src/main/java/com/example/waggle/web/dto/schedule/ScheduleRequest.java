package com.example.waggle.web.dto.schedule;

import lombok.*;

import java.time.LocalDateTime;

public class ScheduleRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Post {
        private Long id;
        private String title;
        private String content;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }

}
