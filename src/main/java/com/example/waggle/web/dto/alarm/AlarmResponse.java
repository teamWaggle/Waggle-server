package com.example.waggle.web.dto.alarm;

import com.example.waggle.domain.notification.entity.alarm.alarmArgs.AlarmArgs;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class AlarmResponse {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AlarmViewDto {
        private Long alarmId;
        private String text;
        private AlarmArgs alarmArgs;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AlarmListDto {
        private List<AlarmViewDto> alarmViewDtoList;
    }
}
