package com.example.waggle.comment.domain;

import com.example.waggle.commons.component.auditing.BaseEntity;
import com.example.waggle.board.Board;
import com.example.waggle.reply.domain.Reply;
import com.example.waggle.member.domain.Member;
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


    //@Temporal(TemporalType.TIMESTAMP)
//    private LocalDateTime createdDate;

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
