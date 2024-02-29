package com.example.waggle.global.security.service;

import com.example.waggle.web.dto.member.MemberRequest.MemberCredentialsDto;
import com.example.waggle.global.security.object.JwtToken;
import org.springframework.security.core.Authentication;

public interface TokenService {


    JwtToken login(MemberCredentialsDto loginRequest);

    JwtToken issueTokens(String refreshToken);

    JwtToken generateToken(Authentication authentication);

    Authentication getAuthentication(String accessToken);

    boolean validateToken(String token);

    boolean logout(String refreshToken);

    boolean existsRefreshToken(String refreshToken);
}
