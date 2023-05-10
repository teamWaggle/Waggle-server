package com.example.waggle.dto.member;

import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.member.Pet;
import com.example.waggle.domain.member.Sex;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
public class PetDto {
    private Long id;
    private String name;
    private String breed;
    private Sex sex;
    private LocalDateTime birthday;
    private String profileImg;
    private Member member;

    public Pet toEntity() {
        return Pet.builder()
                .id(id)
                .name(name)
                .breed(breed)
                .sex(sex)
                .birthday(birthday)
                .profileImg(profileImg)
                .member(member)
                .build();

    }

    @Builder
    public PetDto(Long id, String name, String breed, Sex sex, LocalDateTime birthday, String profileImg, Member member) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.sex = sex;
        this.birthday = birthday;
        this.profileImg = profileImg;
        this.member = member;
    }
}
