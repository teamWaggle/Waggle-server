package com.example.waggle.domain.board.hashtag;

import com.example.waggle.component.BaseEntity;
import com.example.waggle.component.BaseTimeEntity;
import com.example.waggle.domain.board.Board;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardHashtag extends BaseEntity {
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
    @Builder
    BoardHashtag(Long id,Board board, Hashtag hashtag) {
        this.id = id;
        this.board = board;
        this.hashtag = hashtag;
        board.getBoardHashtags().add(this);
    }

//    //관계 취소(ex. 보드에서 해시태그 삭제)
//    public void cancelHashtag() {
//        board.getBoardHashtags().remove(this);
//        //hashtag.getBoardHashtags().remove(this);
//    }

}
