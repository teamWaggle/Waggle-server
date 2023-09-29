package com.example.waggle.myLocate.domain;

import com.example.waggle.commons.component.auditing.BaseEntity;
import com.example.waggle.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyLocate extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "mylocate_id")
    private Long id;

    private String name;
    private String address;

    @Enumerated
    private Locate locate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
