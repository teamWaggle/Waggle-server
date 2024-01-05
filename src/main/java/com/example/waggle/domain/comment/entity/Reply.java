package com.example.waggle.domain.comment.entity;

import com.example.waggle.global.component.auditing.BaseEntity;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.mention.entity.Mention;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @Column(nullable = false)
    private String content;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;


    @Builder.Default
    @OneToMany(mappedBy = "reply",cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Mention> mentions = new ArrayList<>();

    public void changeContent(String content) {
        this.content = content;
    }

    public void addMention(Mention mention) {
        this.getMentions().add(mention);
        mention.changeReply(this);
    }

}
