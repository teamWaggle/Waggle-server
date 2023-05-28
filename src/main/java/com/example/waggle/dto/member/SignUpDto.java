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

    public Member toEntity() {

        return Member.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .address(address)
                .phone(phone)
                .profileImg(profileImg)
                .build();
    }
}
