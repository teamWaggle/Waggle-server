package com.example.waggle.web.dto.schedule;

import com.example.waggle.domain.schedule.entity.ScheduleStatus;
import com.example.waggle.domain.schedule.entity.TeamColor;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ScheduleResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ScheduleDetailDto {
        private Long boardId;
        private Long teamId;
        private TeamColor teamColor;
        private String title;
        private String content;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime createdDate;
        private ScheduleStatus status;
        private MemberSummaryDto member;
        private Boolean isScheduled;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ScheduleListDto {
        private List<ScheduleDetailDto> scheduleList;
        private long scheduleCount;
        private Boolean isFirst;
        private Boolean isLast;
    }

}
