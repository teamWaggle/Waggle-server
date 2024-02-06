package com.example.waggle.domain.conversation;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.mention.entity.Mention;
import com.example.waggle.global.component.auditing.BaseEntity;
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
@DiscriminatorColumn(name = "DTYPE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Conversation extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "conversation_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    protected Member member;
    @Lob
    @Column(nullable = false)
    private String content;

    @Builder.Default
    @OneToMany(mappedBy = "conversation", orphanRemoval = true)
    private List<Mention> mentions = new ArrayList<>();

    public void changeContent(String content) {
        this.content = content;
    }

}
