package com.example.waggle.domain.board.persistence.entity;

import com.example.waggle.domain.auditing.persistence.entity.BaseEntity;
import com.example.waggle.domain.conversation.persistence.entity.Comment;
import com.example.waggle.domain.hashtag.persistence.entity.BoardHashtag;
import com.example.waggle.domain.media.persistence.entity.Media;
import com.example.waggle.domain.member.persistence.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @NotNull
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

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    protected List<Comment> comments = new ArrayList<>();


    public Board(Long id, Member member, String content, List<BoardHashtag> boardHashtags,
                 List<Media> medias) {
        this.id = id;
        this.member = member;
        this.content = content;
        this.boardHashtags = boardHashtags;
        this.medias = medias;
    }

}
