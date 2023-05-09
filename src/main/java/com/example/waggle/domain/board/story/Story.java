package com.example.waggle.domain.board.story;

import com.example.waggle.domain.board.Board;
import jakarta.persistence.Entity;

@Entity
public class Story extends Board {
    private int recommend;
}
