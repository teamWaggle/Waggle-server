package com.example.waggle.domain.member.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class MemberRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class MemberCredentialsDto {

        @NotBlank(message = "이메일을 입력해 주세요.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        @Size(max = 320, message = "이메일은 최대 320자까지 작성할 수 있습니다.")
        private String email;

        @NotBlank(message = "비밀번호를 입력해 주세요.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*]).+$", message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
        @Size(min = 8, max = 64, message = "비밀번호는 최소 8자 이상, 최대 64자 이하여야 합니다.")
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class MemberUpdateDto {

        @NotEmpty(message = "닉네임을 입력해 주세요.")
        @Size(max = 20, message = "닉네임은 최대 20자까지 작성할 수 있습니다.")
        private String nickname;

        @NotBlank(message = "이름을 입력해 주세요.")
        @Pattern(regexp = "^[a-zA-Z가-힣]+$", message = "이름은 공백, 숫자, 특수문자를 포함할 수 없습니다.")
        @Size(max = 20, message = "이름은 최대 20자까지 작성할 수 있습니다.")
        private String name;

        @NotNull(message = "생일을 입력해 주세요.")
        private LocalDate birthday;

        private String memberProfileImg;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class MemberProfileDto {

        @NotEmpty(message = "닉네임을 입력해 주세요.")
        @Size(max = 20, message = "닉네임은 최대 20자까지 작성할 수 있습니다.")
        private String nickname;

        @NotBlank(message = "이름을 입력해 주세요.")
        @Pattern(regexp = "^[a-zA-Z가-힣]+$", message = "이름은 공백, 숫자, 특수문자를 포함할 수 없습니다.")
        private String name;

        @NotNull(message = "생일을 입력해주세요.")
        private LocalDate birthday;

        @NotBlank(message = "유저 URL을 입력해 주세요.")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "유저 URL은 영어 대소문자를 구분하지 않습니다. 한글은 포함할 수 없습니다.")
        @Size(max = 20, message = "유저 URL은 최대 20자까지 작성할 수 있습니다.")
        private String userUrl;

        private String memberProfileImg;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class PasswordDto {

        @NotBlank(message = "비밀번호를 입력해 주세요.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*]).+$", message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
        @Size(min = 8, max = 64, message = "비밀번호는 최소 8자 이상, 최대 64자 이하여야 합니다.")
        private String password;

    }

}
