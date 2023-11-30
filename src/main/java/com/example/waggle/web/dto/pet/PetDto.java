package com.example.waggle.web.dto.pet;

import com.example.waggle.domain.member.domain.Gender;
import com.example.waggle.domain.member.domain.Member;
import com.example.waggle.domain.pet.domain.Pet;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PetDto {
    private Long id;
    @NotNull
    private String name;
    private String breed;
    private Gender gender;
    private LocalDate birthday;
    private String profileImg;
    @NotNull
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
