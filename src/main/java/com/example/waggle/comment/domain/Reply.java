package com.example.waggle.comment.domain;

import com.example.waggle.commons.component.auditing.BaseEntity;
import com.example.waggle.mention.domain.Mention;
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
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "reply_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;


    @Builder.Default
    @OneToMany(mappedBy = "reply",cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Mention> mentions = new ArrayList<>();

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public void addMention(Mention mention) {
        mentions.add(mention);
        mention.setReply(this);
    }


    public void changeContent(String content) {
        this.content = content;
    }

}
