package com.example.waggle.web.dto.schedule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class ScheduleRequest {
    private String title;
    private String content;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
