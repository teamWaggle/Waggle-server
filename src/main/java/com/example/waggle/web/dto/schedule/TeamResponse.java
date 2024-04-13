package com.example.waggle.web.dto.schedule;

import com.example.waggle.domain.schedule.entity.TeamColor;
import com.example.waggle.web.dto.member.MemberResponse.MemberSummaryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        private Integer maxTeamSize;
        private Integer teamSize;
        private MemberSummaryDto leader;
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
        private Integer maxTeamSize;
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
    public class ParticipationStatusResponse {

        private Status status;

        public enum Status {
            PENDING, ACCEPTED, REJECTED, NONE
        }

        public void setStatus(Status status) {
            this.status = status;
        }
    }

}
