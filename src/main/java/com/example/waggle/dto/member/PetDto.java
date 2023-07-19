package com.example.waggle.dto.member;

import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.member.Pet;
import com.example.waggle.domain.member.Gender;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PetDto {
    private Long id;
    private String name;
    private String breed;
    private Gender gender;
    private LocalDate birthday;
    private String profileImg;
    private String username;

    static public PetDto toDto(Pet pet) {
        return PetDto.builder()
                .id(pet.getId())
                .name(pet.getName())
                .breed(pet.getBreed())
                .gender(pet.getGender())
                .birthday(pet.getBirthday())
                .profileImg(pet.getProfileImg())
                .username(pet.getMember().getUsername())
                .build();
    }

    public Pet toEntity(Member member) {
        return Pet.builder()
                .id(id)
                .name(name)
                .breed(breed)
                .gender(gender)
                .birthday(birthday)
                .profileImg(profileImg)
                .member(member)
                .build();
    }
}
