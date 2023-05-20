package com.example.waggle.dto.member;

import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.member.Pet;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {

    private String username;
    private String password;
    private String nickname;
    private String address;
    private String phone;
    private String profileImg;
    @Builder.Default
    private List<PetDto> pets = new ArrayList<>();

    public Member toEntity() {
        List<Pet> petList = new ArrayList<>();
        if (pets != null) {
            petList = pets.stream()
                    .map(petDto -> petDto.toEntity())
                    .collect(Collectors.toList());
        }

        return Member.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .address(address)
                .phone(phone)
                .profileImg(profileImg)
                .pets(petList)
                .build();
    }
}
