package com.example.waggle.dto.board;

import com.example.waggle.domain.board.qna.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSimpleDto {

    private Long questionId;
    private String username;
    private String title;
    private LocalDateTime createTime;
    private int recommendCount;
    private boolean recommendIt;
    @Builder.Default
    private List<String> hashtags = new ArrayList<>();

    static public QuestionSimpleDto toDto(Question question, int recommendCount, boolean recommendIt) {
        return QuestionSimpleDto.builder()
                .questionId(question.getId())
                .username(question.getMember().getUsername())
                .title(question.getTitle())
                .createTime(question.getCreatedDate())
                .recommendCount(recommendCount)
                .recommendIt(recommendIt)
                .hashtags(question.getBoardHashtags().stream()
                        .map(h->h.getHashtag().getTag()).collect(Collectors.toList()))
                .build();
    }
}
