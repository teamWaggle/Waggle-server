package com.example.waggle.web.dto.question;

import com.example.waggle.domain.board.ResolutionStatus;
import com.example.waggle.global.annotation.valid.ValidEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

public class QuestionRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QuestionCreateDto {

        @NotEmpty(message = "질문 내용을 작성해주세요.")
        @Max(1500)
        private String content;

        @NotBlank(message = "질문 제목을 작성해주세요.")
        @Length(min = 5, max = 30)
        private String title;

        private List<String> hashtagList;

        @ValidEnum(target = ResolutionStatus.class)
        private String status;
    }

}