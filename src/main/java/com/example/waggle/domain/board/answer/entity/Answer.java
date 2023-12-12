package com.example.waggle.domain.board.answer.entity;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.question.entity.Question;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@DiscriminatorValue("type_answer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends Board {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void changeAnswer(String content) {
        this.content = content;
    }
}
