package com.example.waggle.domain.board.hashtag;

import com.example.waggle.component.auditing.BaseEntity;
import com.example.waggle.domain.board.Board;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardHashtag extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "board_hashtag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;


    //builder에서 연관관계까지 맺어버리게 만들기


    public void link(Board board, Hashtag hashtag) {
        board.getBoardHashtags().add(this);
        hashtag.getBoardHashtags().add(this);
    }

//    //관계 취소(ex. 보드에서 해시태그 삭제)
//    public void cancelHashtag() {
//        board.getBoardHashtags().remove(this);
//        //hashtag.getBoardHashtags().remove(this);
//    }

}
