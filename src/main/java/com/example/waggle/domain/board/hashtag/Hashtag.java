package com.example.waggle.domain.board.hashtag;

import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Hashtag {
    @Id @GeneratedValue
    @Column(name = "hashtag_id")
    private Long id;

    private String h_content;

    @OneToMany(mappedBy = "board")
    @Builder.Default
    private List<BoardHashtag> boardHashtags = new ArrayList<>();

}
