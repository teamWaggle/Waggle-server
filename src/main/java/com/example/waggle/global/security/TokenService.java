package com.example.waggle.global.security;

import com.example.waggle.web.dto.member.MemberRequest.TemporaryRegisterDto;
import org.springframework.security.core.Authentication;

public interface TokenService {


    JwtToken login(TemporaryRegisterDto request);

    JwtToken issueTokens(String refreshToken);

    JwtToken generateToken(Authentication authentication);

    Authentication getAuthentication(String accessToken);

    boolean validateToken(String token);

    boolean logout(String refreshToken);

    boolean existsRefreshToken(String refreshToken);
}
