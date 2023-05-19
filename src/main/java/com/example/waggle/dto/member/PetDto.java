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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetDto {
    private Long id;
    private String name;
    private String breed;
    private Sex sex;
    private LocalDateTime birthday;
    private String profileImg;
    private Member member;  // 순환 참조 방지 위해 MemberDto 대신 Member 사용

    static public PetDto toDto(Pet pet) {
        return PetDto.builder()
                .id(pet.getId())
                .name(pet.getName())
                .breed(pet.getBreed())
                .sex(pet.getSex())
                .birthday(pet.getBirthday())
                .profileImg(pet.getProfileImg())
                .member(pet.getMember())
                .build();

    }

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
}
