package com.example.waggle.domain.member;

import jakarta.persistence.*;
import lombok.*;

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
    public Pet(Long id, String name, String breed, Sex sex, LocalDateTime birthday, String profileImg, Member member) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.sex = sex;
        this.birthday = birthday;
        this.profileImg = profileImg;
        setMember(member);
    }

    // 연관관계 편의 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getPets().add(this);
    }

}
