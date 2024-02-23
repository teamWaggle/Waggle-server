package com.example.waggle.web.dto.question;

import com.example.waggle.domain.board.ResolutionStatus;
import com.example.waggle.global.annotation.valid.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

public class QuestionRequest {

    @Builder
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {

        @NotEmpty(message = "질문 내용을 작성해주세요.")
        @Size(min = 1, max = 500)
        private String content;

        @NotBlank(message = "질문 제목을 작성해주세요.")
        @Length(min = 5, max = 30)
        private String title;

        @Builder.Default
        private List<String> hashtags = new ArrayList<>();

        @ValidEnum(target = ResolutionStatus.class)
        private String status;
    }
}