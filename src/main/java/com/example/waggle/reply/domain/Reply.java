package com.example.waggle.reply.domain;

import com.example.waggle.comment.domain.Comment;
import com.example.waggle.commons.component.auditing.BaseEntity;
import com.example.waggle.memberMention.domain.MemberMention;
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

    //who write this reply
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    //@Temporal(TemporalType.TIMESTAMP)
    //private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;


    //mention targets
    @Builder.Default
    @OneToMany(mappedBy = "reply",cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MemberMention> memberMentions = new ArrayList<>();

    //protected -> public
    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public void addMemberMention(MemberMention memberMention) {
        memberMentions.add(memberMention);
        memberMention.setReply(this);
    }


    public void changeContent(String content) {
        this.content = content;
    }

}
