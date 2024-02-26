package com.example.waggle.web.dto.schedule;

import com.example.waggle.domain.schedule.entity.ScheduleStatus;
import com.example.waggle.domain.schedule.entity.TeamColor;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ScheduleResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleDetailDto {
        private Long boardId;
        private Long teamId;
        private TeamColor teamColor;
        private String title;
        private String content;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private LocalDateTime createdDate;
        private ScheduleStatus status;
        private MemberSummaryDto member;
        private Boolean isOwner;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleListDto {
        private List<ScheduleDetailDto> scheduleList;
        private long scheduleCount;
        private Boolean isFirst;
        private Boolean isLast;
    }

}
