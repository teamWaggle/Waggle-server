package com.example.waggle.web.dto.schedule;

import com.example.waggle.web.dto.member.MemberResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class ScheduleResponse {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class DetailDto {
        private Long scheduleId;
        private Long teamId;
        private String title;
        private String content;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private MemberResponse.SummaryDto member;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class ListDto {
        List<DetailDto> schedules;
    }

}
