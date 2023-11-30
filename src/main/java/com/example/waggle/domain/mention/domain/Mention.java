package com.example.waggle.domain.mention.domain;

import com.example.waggle.global.component.auditing.BaseEntity;
import com.example.waggle.domain.comment.domain.Reply;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mention extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "member_mention_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    private Reply reply;

    private String username;

    public void setReply(Reply reply) {
        this.reply = reply;
    }

}
