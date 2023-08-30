package com.example.waggle.dto.member;

import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.member.Pet;
import com.example.waggle.domain.member.UploadFile;
import com.example.waggle.dto.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

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

    @NotBlank(message = "아이디를 작성해주세요.", groups = ValidationGroups.NotEmpty.class)
    @Length(min = 2, max = 10, message = "아이디 길이는 최소 2자에서 최대 10자입니다.", groups = ValidationGroups.LimitCount.class)
    private String username;
    @NotBlank(message = "비밀번호를 작성해주세요.", groups = ValidationGroups.NotEmpty.class)
    @Length(min = 10, message = "비밀번호는 최소 10자입니다.", groups = ValidationGroups.LimitCount.class)
    private String password;
    private String nickname;
    private String address;
    @NotBlank(message = "전화번호를 입력해주세요", groups = ValidationGroups.NotEmpty.class)
    private String phone;
    private MultipartFile profileImg;

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
                .roles(roles)
                .build();
    }
}
