package com.example.waggle.pet.domain;

import com.example.waggle.commons.component.auditing.BaseEntity;
import com.example.waggle.pet.dto.PetDto;
import com.example.waggle.member.domain.Gender;
import com.example.waggle.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Pet extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "pet_id")
    private Long id;

    private String name;

    private String breed;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthday;

    private String profileImg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void update(PetDto petDto) {
        this.name = petDto.getName();
        this.breed = petDto.getBreed();
        this.gender = petDto.getGender();
        this.birthday = petDto.getBirthday();
        this.profileImg = petDto.getProfileImg();
    }

}
