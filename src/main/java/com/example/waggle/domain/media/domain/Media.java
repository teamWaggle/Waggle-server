package com.example.waggle.domain.media.domain;

import com.example.waggle.global.component.auditing.BaseTimeEntity;
import com.example.waggle.domain.board.Board;
import com.example.waggle.global.component.file.UploadFile;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
    private UploadFile uploadFile;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;


    public void linkBoard(Board board) {
        this.board = board;
        board.getMedias().add(this);
    }

    public void updateUploadFile(UploadFile uploadFile) {
        this.uploadFile = uploadFile;
    }

}
