package com.example.waggle.domain.board;

import com.example.waggle.component.auditing.BaseEntity;
import com.example.waggle.domain.board.comment.Comment;
import com.example.waggle.domain.board.hashtag.BoardHashtag;
import com.example.waggle.domain.member.Member;
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

    //@Temporal(TemporalType.TIMESTAMP) -> 현 jpa버전에서는 자동매핑해주기 때문에 필요없다고 함
//    protected LocalDateTime createdDate;

    @Lob
    protected String content;

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST, orphanRemoval = true)
    protected List<BoardHashtag> boardHashtags = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST, orphanRemoval = true)
    protected List<Media> medias = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board", orphanRemoval = true)
    protected List<Comment> comments = new ArrayList<>();


    public Board(Long id, Member member, String content, List<BoardHashtag> boardHashtags,
                 List<Media> medias, List<Comment> comments) {
        this.id = id;
        this.member = member;
        this.content = content;
        this.boardHashtags = boardHashtags;
        this.medias = medias;
        this.comments = comments;
    }

}
