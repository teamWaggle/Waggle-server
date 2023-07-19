package com.example.waggle.domain.board;

import com.example.waggle.component.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Media extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "media_id")
    private Long id;
    private String url;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    Media(String url, Board board) {
        this.url = url;
        setBoard(board);
    }

    protected void setBoard(Board board) {
        this.board = board;
        board.getMedias().add(this);
    }

    public void changeURL(String url) {
        this.url = url;
    }


}
