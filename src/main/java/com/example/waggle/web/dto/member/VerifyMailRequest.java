package com.example.waggle.web.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class VerifyMailRequest {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConfirmationDto {
        private String email;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthDto {
        private String email;
        private String authCode;
    }

}
