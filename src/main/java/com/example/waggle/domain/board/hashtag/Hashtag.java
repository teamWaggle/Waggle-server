package com.example.waggle.domain.board.hashtag;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hashtag {
    @Id @GeneratedValue
    @Column(name = "hashtag_id")
    private Long id;

    private String h_content;

    @OneToMany(mappedBy = "board")
    private List<BoardHashtag> boardHashtags;

    @Builder
    Hashtag(Long id, String h_content) {
        this.id = id;
        this.h_content = h_content;
        this.boardHashtags = new ArrayList<>();
    }
}
