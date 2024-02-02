package com.example.waggle.web.dto.schedule;

import com.example.waggle.web.dto.member.MemberResponse;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class TeamResponse {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class DetailDto {
        private Long teamId;
        private String name;
        private String description;
        private String coverImageUrl;
        private String colorScheme;
        private Integer maxTeamSize;
        private MemberResponse.SummaryDto leader;
        private List<MemberResponse.SummaryDto> teamMember;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class SummaryDto {
        private Long teamId;
        private String name;
        private String coverImageUrl;
        private String colorScheme;
        private Integer maxTeamSize;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class ListDto {
        @Builder.Default
        private List<SummaryDto> teams = new ArrayList<>();
        private long totalQuestions;
        private boolean isFirst;
        private boolean isLast;
    }
}
