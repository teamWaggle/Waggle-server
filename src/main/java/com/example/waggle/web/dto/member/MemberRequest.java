package com.example.waggle.web.dto.member;

import com.example.waggle.global.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

public class MemberRequest {

    @Builder
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequestDto {

        @NotBlank(message = "아이디를 작성해주세요.")
        private String username;
        @NotBlank(message = "비밀번호를 작성해주세요.")
        private String password;

    }

    @Builder
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterRequestDto {

        private String email;

        @NotBlank(message = "아이디를 작성해주세요.", groups = ValidationGroups.NotEmpty.class)
        @Length(min = 2, max = 10, message = "아이디 길이는 최소 2자에서 최대 10자입니다.", groups = ValidationGroups.LimitCount.class)
        private String username;
        @NotBlank(message = "비밀번호를 작성해주세요.", groups = ValidationGroups.NotEmpty.class)
        @Length(min = 10, message = "비밀번호는 최소 10자입니다.", groups = ValidationGroups.LimitCount.class)
        private String password;
        @NotBlank
        private String nickname;
        @NotBlank
        private String email;
        private String address;
        @NotBlank(message = "전화번호를 입력해주세요", groups = ValidationGroups.NotEmpty.class)
        private String phone;

//        @Builder.Default
//        private List<PetDto> pets = new ArrayList<>();

        @Builder.Default
        private List<String> roles = new ArrayList<>();

    }

    @Builder
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PutDto {

        @NotBlank(message = "비밀번호를 작성해주세요.", groups = ValidationGroups.NotEmpty.class)
        @Length(min = 10, message = "비밀번호는 최소 10자입니다.", groups = ValidationGroups.LimitCount.class)
        private String password;
        private String nickname;
        private String address;
        @NotBlank(message = "전화번호를 입력해주세요", groups = ValidationGroups.NotEmpty.class)
        private String phone;
        private String profileImgUrl;

        @Builder.Default
        private List<String> roles = new ArrayList<>();

    }
}
