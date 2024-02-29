package com.example.waggle.domain.pet.entity;

import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.component.auditing.BaseEntity;
import com.example.waggle.web.dto.pet.PetRequest;
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
public class Pet extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "pet_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String breed;

    private String description;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String age;

    private String profileImgUrl;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void update(PetRequest updatePetRequest) {
        this.name = updatePetRequest.getName();
        this.breed = updatePetRequest.getBreed();
        this.description = updatePetRequest.getDescription();
        this.gender = Gender.valueOf(updatePetRequest.getGender());
        this.age = updatePetRequest.getAge();
        this.profileImgUrl = updatePetRequest.getProfileImgUrl();
    }

}
