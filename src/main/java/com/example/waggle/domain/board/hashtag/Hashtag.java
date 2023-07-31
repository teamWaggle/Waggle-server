package com.example.waggle.domain.board.hashtag;

import com.example.waggle.component.auditing.BaseEntity;
import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hashtag extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "hashtag_id")
    private Long id;

    private String tag;

    @OneToMany(mappedBy = "hashtag")
    @Builder.Default
    private List<BoardHashtag> boardHashtags = new ArrayList<>();


}
