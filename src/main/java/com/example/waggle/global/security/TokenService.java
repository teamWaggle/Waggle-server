package com.example.waggle.global.security;

import com.example.waggle.web.dto.member.MemberRequest.MemberCredentialsDto;
import org.springframework.security.core.Authentication;

public interface TokenService {


    JwtToken login(MemberCredentialsDto memberLoginRequest);

    JwtToken issueTokens(String refreshToken);

    JwtToken generateToken(Authentication authentication);

    Authentication getAuthentication(String accessToken);

    boolean validateToken(String token);

    boolean logout(String refreshToken);

    boolean existsRefreshToken(String refreshToken);
}
