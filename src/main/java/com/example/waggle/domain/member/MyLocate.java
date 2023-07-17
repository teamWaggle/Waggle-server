package com.example.waggle.domain.member;

import com.example.waggle.component.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyLocate extends BaseTimeEntity {

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
