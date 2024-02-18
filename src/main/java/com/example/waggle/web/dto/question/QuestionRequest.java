package com.example.waggle.web.dto.question;

import com.example.waggle.domain.board.ResolutionStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

public class QuestionRequest {

    @Builder
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {

        @NotEmpty(message = "질문 내용을 작성해주세요.")
        @Max(1500)
        private String content;

        @NotBlank(message = "질문 제목을 작성해주세요.")
        @Length(min = 5, max = 30)
        private String title;

        @Builder.Default
        private List<String> hashtags = new ArrayList<>();

        private ResolutionStatus status;
    }
}