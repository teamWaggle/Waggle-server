package com.example.waggle.domain.comment.entity;

import com.example.waggle.global.component.auditing.BaseEntity;
import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.member.entity.Member;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Lob
    private String content;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder.Default
    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();


    //==연관관계 메서드==//
//    public void changeBoard(Board board) {
//        this.board = board;
//        board.getComments().add(this);
//    }

    public void addReply(Reply reply) {
        replies.add(reply);
        reply.setComment(this);
    }

    public void changeContent(String content) {
        this.content = content;
    }


}