package com.example.waggle.domain.board.comment;

import com.example.waggle.component.auditing.BaseEntity;
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
public class MemberMention extends BaseEntity {
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

    protected void setReply(Reply reply) {
        this.reply = reply;
    }

}
