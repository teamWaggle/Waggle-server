package com.example.waggle.domain.board.qna;

import com.example.waggle.domain.board.Board;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Question extends Board {
    private String title;

    @OneToMany(mappedBy = "question")
    private List<Answer> answer;
}
