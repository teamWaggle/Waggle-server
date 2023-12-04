package com.example.waggle.web.dto.question;

import com.example.waggle.domain.board.question.entity.Question;
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
public class QuestionSummaryDto {

    private Long id;
    private String username;
    private String title;
    private LocalDateTime createTime;
    private int recommendCount;
    private boolean recommendIt;
    @Builder.Default
    private List<String> hashtags = new ArrayList<>();

    static public QuestionSummaryDto toDto(Question question) {
        return QuestionSummaryDto.builder()
                .id(question.getId())
                .username(question.getMember().getUsername())
                .title(question.getTitle())
                .createTime(question.getCreatedDate())
                .hashtags(question.getBoardHashtags().stream()
                        .map(h->h.getHashtag().getTag()).collect(Collectors.toList()))
                .build();
    }

    public void linkRecommend(int count, boolean recommendIt) {
        this.recommendCount =count;
        this.recommendIt = recommendIt;
    }
}
