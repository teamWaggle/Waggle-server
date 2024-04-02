package com.example.waggle.web.dto.schedule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class ScheduleRequest {
    private String title;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;
    @Schema(description = "Start time in HH:mm format")
    private String startTime;
    @Schema(description = "End time in HH:mm format")
    private String endTime;
}
