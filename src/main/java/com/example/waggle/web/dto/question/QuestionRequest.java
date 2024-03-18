package com.example.waggle.web.dto.question;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class QuestionRequest {
    @NotEmpty(message = "질문 내용을 작성해주세요.")
    @Size(max = 1500)
    private String content;

    @NotBlank(message = "질문 제목을 작성해주세요.")
    @Length(min = 5, max = 30)
    private String title;

    private List<String> hashtagList;
    private List<String> mediaList;
}