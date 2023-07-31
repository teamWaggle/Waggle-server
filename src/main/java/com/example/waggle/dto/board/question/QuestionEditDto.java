package com.example.waggle.dto.board.question;

import com.example.waggle.domain.board.boardType.Question;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionEditDto {

    private Long id;
    private String content;
    private String title;

    @Builder.Default
    private List<String> medias = new ArrayList<>();
    @Builder.Default
    private List<String> hashtags = new ArrayList<>();

    static public QuestionEditDto toDto(Question question) {
        return QuestionEditDto.builder()
                .id(question.getId())
                .content(question.getContent())
                .title(question.getTitle())
                .medias(question.getMedias()
                        .stream().map(m -> m.getUrl()).collect(Collectors.toList()))
                .hashtags(question.getBoardHashtags()
                        .stream().map(bh -> bh.getHashtag().getTag()).collect(Collectors.toList()))
                .build();
    }
}
