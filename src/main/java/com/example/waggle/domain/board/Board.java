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
public class Board {
    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;

    @Lob
    private String content;

    @OneToMany(mappedBy = "board")
    private List<BoardHashtag> boardHashtags;

    @OneToMany(mappedBy = "board")
    private List<Media> medias;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments;

}
