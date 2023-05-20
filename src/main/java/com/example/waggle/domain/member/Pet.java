package com.example.waggle.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pet {
    @Id @GeneratedValue
    @Column(name = "pet_id")
    private Long id;

    private String name;

    private String breed;

    @Enumerated
    private Sex sex;

    private LocalDateTime birthday;

    private String profileImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    Pet(String name, String breed, Sex sex, String profileImg, Member member) {
        this.name = name;
        this.breed =breed;
        this.sex = sex;
        this.profileImg = profileImg;
        this.member = member;
    }
}
