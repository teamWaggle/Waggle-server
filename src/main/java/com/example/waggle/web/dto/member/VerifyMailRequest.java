package com.example.waggle.web.dto.member;

import lombok.*;


public class VerifyMailRequest {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthDto {
        private String email;
        private String authCode;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConfirmationDto {
        private String email;
    }

}
