package com.example.waggle.domain.hashtag.domain;

import com.example.waggle.commons.component.auditing.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @OneToMany(mappedBy = "hashtag",cascade = CascadeType.PERSIST, orphanRemoval = true)
    @Builder.Default
    private List<BoardHashtag> boardHashtags = new ArrayList<>();


}
