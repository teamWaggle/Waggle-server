package com.example.waggle.web.dto.answer;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AnswerRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerCreateDto {
        @NotNull(message = "답변 내용을 작성해주세요.")
        @Max(1500)
        private String content;
    }

}
