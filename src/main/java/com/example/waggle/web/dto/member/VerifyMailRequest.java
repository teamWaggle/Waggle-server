package com.example.waggle.web.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class VerifyMailRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailSendDto {
        private String email;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailVerificationDto {
        private String email;
        private String authCode;
    }

}
