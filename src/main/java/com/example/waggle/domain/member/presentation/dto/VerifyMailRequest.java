package com.example.waggle.domain.member.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class VerifyMailRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class EmailSendDto {

        @NotBlank(message = "이메일을 작성해주세요.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        @Size(max = 320, message = "이메일은 최대 320자까지 작성할 수 있습니다.")
        private String email;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class EmailVerificationDto {

        @NotBlank(message = "이메일을 작성해주세요.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        @Size(max = 320, message = "이메일은 최대 320자까지 작성할 수 있습니다.")
        private String email;

        @NotBlank(message = "인증 코드를 작성해주세요.")
        @Pattern(regexp = "\\d{6}", message = "인증 코드는 6자리 숫자여야 합니다.")
        private String authCode;
    }

}
