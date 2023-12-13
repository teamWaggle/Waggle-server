package com.example.waggle.domain.board;

import com.example.waggle.domain.hashtag.entity.BoardHashtag;
import com.example.waggle.domain.media.entity.Media;
import com.example.waggle.domain.member.entity.Member;
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
@DiscriminatorColumn(name = "DTYPE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Board extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    protected Member member;

    @Lob
    @Column(nullable = false)
    protected String content;

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST, orphanRemoval = true)
    protected List<BoardHashtag> boardHashtags = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST, orphanRemoval = true)
    protected List<Media> medias = new ArrayList<>();


    public Board(Long id, Member member, String content, List<BoardHashtag> boardHashtags,
                 List<Media> medias) {
        this.id = id;
        this.member = member;
        this.content = content;
        this.boardHashtags = boardHashtags;
        this.medias = medias;
    }

}
