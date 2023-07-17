package com.example.waggle.domain.board.comment;

import com.example.waggle.component.BaseTimeEntity;
import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "reply_id")
    private Long id;

    //who write this reply
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    private int orders;

    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;


    //mention targets
    @OneToMany(mappedBy = "reply",cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MemberMention> memberMentions = new ArrayList<>();

    @Builder
    Reply(Long id, Member member, String content, int orders, LocalDateTime createdDate,
            Comment comment, List<MemberMention> mentionedMembers) {
        this.id = id;
        this.member = member;
        this.content = content;
        this.orders = orders;
        this.createdDate = createdDate;
        setComment(comment);
        if (mentionedMembers != null) {
            for (MemberMention mentionedMember : mentionedMembers) {
                addMemberMention(mentionedMember);
            }
        }
    }

    protected void setComment(Comment comment) {
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
