package com.example.waggle.domain.board.question.entity;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.ResolutionStatus;
import com.example.waggle.web.dto.question.QuestionRequest;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
        this.status = ResolutionStatus.valueOf(updateQuestionRequest.getStatus());
    }

    public void changeStatus(ResolutionStatus status) {
        this.status = status;
    }

    public void increaseViewCount() {
        viewCount++;
    }

}
