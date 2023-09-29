package com.example.waggle.board.question.dto;

import com.example.waggle.board.question.domain.Answer;
import com.example.waggle.member.domain.Member;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerWriteDto {

    @NotNull(message = "답변 내용을 작성해주세요.")
    @Max(1500)
    private String content;

    @Builder.Default
    private List<String> medias = new ArrayList<>();
    @Builder.Default
    private List<String> hashtags = new ArrayList<>();

    public Answer toEntity(Member member) {
        return Answer.builder()
                .content(content)
                .member(member)
                .build();
    }
}
