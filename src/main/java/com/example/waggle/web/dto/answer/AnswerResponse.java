package com.example.waggle.web.dto.answer;

import com.example.waggle.web.dto.member.MemberResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AnswerResponse {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ViewDto {

        private Long id;
        private String content;
        private LocalDateTime createdDate;
        private int recommendCount;
        private boolean isRecommend;
        private boolean isMine;

        @Builder.Default
        private List<String> hashtags = new ArrayList<>();

        @Builder.Default
        private List<String> medias = new ArrayList<>();

        private MemberResponse.SummaryDto member;

    }

    @Builder
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListDto {
        @Builder.Default
        private List<AnswerResponse.ViewDto> AnswerList = new ArrayList<>();
        private long totalAnswers;
        private boolean isFirst;
        private boolean isLast;
    }
}
