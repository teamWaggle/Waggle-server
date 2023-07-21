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
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SignUpDto {

    private String username;
    private String password;
    private String nickname;
    private String address;
    private String phone;
    private String profileImg;

    @Builder.Default
    private List<PetDto> pets = new ArrayList<>();

    @Builder.Default
    private List<String> roles = new ArrayList<>();

    public Member toEntity(String encodedPassword, List<String> roles) {

        return Member.builder()
                .username(username)
                .password(encodedPassword)
                .nickname(nickname)
                .address(address)
                .phone(phone)
                .profileImg(profileImg)
                .roles(roles)
                .build();
    }
}
