package com.example.waggle.domain.board.comment;

import com.example.waggle.component.BaseTimeEntity;
import com.example.waggle.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberMention extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "member_mention_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    private Reply reply;

//    //mention target
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;

    /**
     * mention entity에 mention하고자 하는 사람의
     * entity 정보를 굳이 다 끌고 올필요가 있을까?
     * username만 가지고 있어도 충분하지 않을까
     */
    private String username;

    @Builder
    MemberMention(Long id, String username, Reply reply) {
        this.id = id;
        this.username = username;
        setReply(reply);
    }

    protected void setReply(Reply reply) {
        this.reply = reply;
    }

}
