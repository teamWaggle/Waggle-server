package com.example.waggle.domain.board;

import com.example.waggle.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Board {
    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    protected Member member;

    @Temporal(TemporalType.TIMESTAMP)
    protected LocalDateTime createdDate;

    @Lob
    protected String content;

    @OneToMany(mappedBy = "board")
    protected List<BoardHashtag> boardHashtags;

    @OneToMany(mappedBy = "board")
    protected List<Media> medias;

    @OneToMany(mappedBy = "board")
    protected List<Comment> comments;

}
