package com.example.waggle.board.answer.domain;

import com.example.waggle.board.question.domain.Question;
import com.example.waggle.board.Board;
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


    //protected -> public change
    public void setQuestion(Question question) {
        this.question = question;
    }

    public void changeAnswer(String content) {
        this.content = content;
    }
}
