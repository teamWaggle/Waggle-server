package com.example.waggle.web.dto.question;

import com.example.waggle.web.dto.answer.AnswerDetailDto;
import com.example.waggle.web.dto.comment.CommentViewDto;
import com.example.waggle.web.dto.reply.ReplyViewDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class QuestionResponse {

    @Builder
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionSummaryDto {
        private Long id;
        private String username;
        private String title;
        private LocalDateTime createTime;
        private int recommendCount;
        private boolean recommendIt;
        @Builder.Default
        private List<String> hashtags = new ArrayList<>();
    }

    @Builder
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionsListDto {
        @Builder.Default
        private List<QuestionSummaryDto> questionsList = new ArrayList<>();
        private long totalQuestions;
        private boolean isFirst;
        private boolean isLast;
    }

    @Builder
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionDetailDto {
        private Long id;
        private String content;
        private String username;
        private String title;
        private LocalDateTime createDate;
        private int recommendCount;
        private boolean recommendIt;

        @Builder.Default
        private List<String> medias = new ArrayList<>();
        @Builder.Default
        private List<CommentViewDto> comments = new ArrayList<>();
        @Builder.Default
        private List<ReplyViewDto> replies = new ArrayList<>();
        @Builder.Default
        private List<String> hashtags = new ArrayList<>();
        @Builder.Default
        private List<AnswerDetailDto> answers = new ArrayList<>();
    }

}
