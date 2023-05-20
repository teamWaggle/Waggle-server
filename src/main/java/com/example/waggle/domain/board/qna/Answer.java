package com.example.waggle.domain.board.qna;

import com.example.waggle.domain.board.Board;
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

    protected void setQuestion(Question question) {
        this.question = question;
    }
}
