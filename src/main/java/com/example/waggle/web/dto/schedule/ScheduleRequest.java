package com.example.waggle.web.dto.schedule;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class ScheduleRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class ScheduleRequestDto {
        private String title;
        private String content;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }

}
