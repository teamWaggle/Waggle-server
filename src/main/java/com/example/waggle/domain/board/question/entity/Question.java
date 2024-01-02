package com.example.waggle.domain.board.question.entity;

import com.example.waggle.domain.board.Board;
import com.example.waggle.web.dto.question.QuestionRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
    private Status status;


    public void changeQuestion(QuestionRequest.Put request) {
        this.content = request.getContent();
        this.title = request.getTitle();
        this.status = request.getStatus();
    }

    public void changeStatus(Status status) {
        this.status = status;
    }

    public enum Status{
        RESOLVED,UNRESOLVED
    }
}
