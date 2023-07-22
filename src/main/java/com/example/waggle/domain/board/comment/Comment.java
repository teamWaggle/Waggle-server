package com.example.waggle.domain.board.comment;

import com.example.waggle.component.auditing.BaseEntity;
import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
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

    private int orders;

    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "comment", orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();

    @Builder
    Comment(Long id, Member member, String content, int orders, LocalDateTime createdDate,
            Board board, List<Reply> replies) {
        this.id = id;
        this.member = member;
        this.content = content;
        this.orders = orders;
        this.createdDate = createdDate;
        //this.board = board;
        changeBoard(board);
        if (replies != null) {
            for (Reply reply : replies) {
                addReply(reply);
            }
        }
    }

    //==연관관계 메서드==//
    public void changeBoard(Board board) {
        this.board = board;
        board.getComments().add(this);
    }

    public void addReply(Reply reply) {
        replies.add(reply);
        reply.setComment(this);
    }

    public void changeContent(String content) {
        this.content = content;
    }


}
