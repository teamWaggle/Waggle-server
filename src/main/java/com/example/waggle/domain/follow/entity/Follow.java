package com.example.waggle.domain.follow.entity;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.component.auditing.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "follow")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "to_member_id")
    private Member toMember;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "from_member_id")
    private Member fromMember;

}
