package com.example.waggle.domain.schedule.presentation.dto.team;

import com.example.waggle.domain.member.presentation.dto.MemberResponse.MemberSummaryDto;
import com.example.waggle.domain.schedule.persistence.entity.TeamColor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class TeamResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class TeamDetailDto {
        private Long teamId;
        private String name;
        private String description;
        private String coverImageUrl;
        private TeamColor teamColor;
        private Integer teamSize;
        private Boolean isPrivate;
        private MemberSummaryDto teamLeader;
        private List<MemberSummaryDto> teamMemberList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class TeamSummaryDto {
        private Long teamId;
        private String name;
        private String coverImageUrl;
        private String description;
        private TeamColor teamColor;
        private Integer teamSize;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class TeamSummaryListDto {
        private List<TeamSummaryDto> teamList;
        private long teamCount;
        private Boolean isFirst;
        private Boolean isLast;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ParticipationStatusResponse {

        private Status status;
    }

}
