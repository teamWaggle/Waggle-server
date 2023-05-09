package com.example.waggle.domain.board.qna;

import com.example.waggle.domain.board.Board;
import jakarta.persistence.*;

@Entity
public class Answer extends Board {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;
}
