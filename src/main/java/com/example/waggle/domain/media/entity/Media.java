package com.example.waggle.domain.media.entity;

import com.example.waggle.domain.board.Board;
import com.example.waggle.global.component.auditing.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Media extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id")
    private Long id;
    @Column(nullable = false)
    private String uploadFile;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public void linkBoard(Board board) {
        this.board = board;
        board.getMedias().add(this);
    }

    public void updateUploadFile(String uploadFile) {
        this.uploadFile = uploadFile;
    }

}
