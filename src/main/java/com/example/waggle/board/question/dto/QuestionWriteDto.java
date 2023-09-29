package com.example.waggle.board.question.dto;

import com.example.waggle.board.question.domain.Question;
import com.example.waggle.member.domain.Member;
import com.example.waggle.commons.validation.UpdateCheck;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionWriteDto {

    @NotEmpty(message = "질문 내용을 작성해주세요.")
    @Max(1500)
    private String content;
    @NotBlank(message = "질문 제목을 작성해주세요.")
    @Length(min = 5, max = 30)
    private String title;
    @NotBlank(groups = UpdateCheck.class)
    private Long id;

    @Builder.Default
    private List<String> medias = new ArrayList<>();
    @Builder.Default
    private List<String> hashtags = new ArrayList<>();

    public Question toEntity(Member member) {
        return Question.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
    }

    public QuestionWriteDto toDto(Question question) {
        return QuestionWriteDto.builder()
                .id(question.getId())
                .content(question.getContent())
                .title(question.getTitle())
                .medias(question.getMedias().stream()
                        .map(m -> m.getUrl()).collect(Collectors.toList()))
                .hashtags(question.getBoardHashtags().stream()
                        .map(bh -> bh.getHashtag().getTag()).collect(Collectors.toList()))
                .build();
    }
}
