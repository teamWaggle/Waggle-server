package com.example.waggle.domain.conversation.entity;

import com.example.waggle.domain.conversation.Conversation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@DiscriminatorValue("type_reply")
@Table(name = "reply")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends Conversation {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

}
