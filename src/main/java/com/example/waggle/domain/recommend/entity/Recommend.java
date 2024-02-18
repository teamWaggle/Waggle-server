package com.example.waggle.domain.recommend.entity;

import com.example.waggle.global.component.auditing.BaseEntity;
import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recommend extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "recommend_id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

}
