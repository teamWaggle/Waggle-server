package com.example.waggle.web.dto.member;

import com.example.waggle.global.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;


public class MemberRequest {

    @Builder
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccessDto {
        @NotBlank
        private String email;

        @NotBlank(message = "비밀번호를 작성해주세요.", groups = ValidationGroups.NotEmpty.class)
        @Length(min = 10, message = "비밀번호는 최소 10자입니다.", groups = ValidationGroups.LimitCount.class)
        private String password;
    }

    @Builder
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Put {

        private String nickname;
        private String name;
        @NotBlank(message = "비밀번호를 작성해주세요.", groups = ValidationGroups.NotEmpty.class)
        @Length(min = 10, message = "비밀번호는 최소 10자입니다.", groups = ValidationGroups.LimitCount.class)
        private String password;
        private LocalDate birthday;
        private String profileImgUrl;
    }

    @Builder
    @Setter
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterDto {

        private String nickname;
        private String name;
        private LocalDate birthday;
        private String userUrl;
        private String profileImgUrl;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FindEmailDto {
        private String name;
        private LocalDate birthday;
    }
}
