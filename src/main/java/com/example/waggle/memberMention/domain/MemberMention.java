package com.example.waggle.memberMention.domain;

import com.example.waggle.commons.component.auditing.BaseEntity;
import com.example.waggle.comment.domain.Reply;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    private String username;

    //protected -> public
    public void setReply(Reply reply) {
        this.reply = reply;
    }

}
