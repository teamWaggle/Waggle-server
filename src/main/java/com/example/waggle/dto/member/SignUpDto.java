package com.example.waggle.dto.member;

import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.member.UploadFile;
import com.example.waggle.dto.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SignUpDto{

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
    private UploadFile profileImg;

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
    public void changeProfileImg(UploadFile profileImg) {
        this.profileImg = profileImg;
    }
}
