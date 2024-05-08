package com.example.waggle.security.jwt.service;

import com.example.waggle.domain.member.presentation.dto.MemberRequest.MemberCredentialsDto;
import com.example.waggle.security.jwt.dto.JwtToken;
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
