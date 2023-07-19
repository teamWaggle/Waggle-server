package com.example.waggle.domain;

import com.example.waggle.component.BaseEntity;
import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "recommend")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    Like(Long id, Member member, Board board) {
        this.id = id;
        this.member = member;
        setBoard(board);
    }

    protected void setBoard(Board board) {
        this.board = board;
        board.getLikes().add(this);
    }
}
