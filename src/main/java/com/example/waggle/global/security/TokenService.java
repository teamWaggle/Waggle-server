package com.example.waggle.global.security;

import com.example.waggle.web.dto.member.MemberRequest;
import org.springframework.security.core.Authentication;

public interface TokenService {

    JwtToken login(MemberRequest.LoginDto request);

    JwtToken issueTokens(String refreshToken);

    JwtToken generateToken(Authentication authentication);

    Authentication getAuthentication(String accessToken);

    boolean validateToken(String token);

    boolean logout(String refreshToken);

}
