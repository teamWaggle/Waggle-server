package com.example.waggle.web.dto.answer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class AnswerRequest {
    @NotNull(message = "답변 내용을 작성해주세요.")
    @Size(max = 1500)
    private String content;
}
