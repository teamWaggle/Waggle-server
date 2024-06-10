package com.example.waggle.domain.board.persistence.entity;

import com.example.waggle.domain.board.presentation.dto.question.QuestionRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@ToString
@Entity
@Getter
@SuperBuilder
@DiscriminatorValue("type_question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends Board {
    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResolutionStatus status;

    @Builder.Default
    private int viewCount = 0;

    public void changeQuestion(QuestionRequest updateQuestionRequest) {
        this.content = updateQuestionRequest.getContent();
        this.title = updateQuestionRequest.getTitle();
    }

    public void changeStatus(ResolutionStatus status) {
        this.status = status;
    }

}
