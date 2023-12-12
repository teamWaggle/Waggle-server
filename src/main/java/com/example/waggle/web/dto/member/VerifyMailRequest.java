package com.example.waggle.web.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class VerifyMailRequest {

    private final String email;
    private final String authCode;

}
