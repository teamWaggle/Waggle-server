package com.example.waggle.domain.follow.persistence.entity;

import com.example.waggle.domain.auditing.persistence.entity.BaseEntity;
import com.example.waggle.domain.member.persistence.entity.Member;
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
