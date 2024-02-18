package com.example.waggle.domain.conversation.entity;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.conversation.Conversation;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@DiscriminatorValue("type_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends Conversation {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder.Default
    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    protected List<Reply> replies = new ArrayList<>();

}
