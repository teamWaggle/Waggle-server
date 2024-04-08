package com.example.waggle.domain.hashtag.entity;

import com.example.waggle.global.component.auditing.BaseEntity;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @Builder.Default
    private List<BoardHashtag> boardHashtags = new ArrayList<>();


}
