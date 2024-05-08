package com.example.waggle.security.oauth.handler;

import com.example.waggle.domain.member.persistence.entity.Role;
import com.example.waggle.global.util.CookieUtil;
import com.example.waggle.security.jwt.dto.JwtToken;
import com.example.waggle.security.jwt.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collection;


@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${app.oauth2.authorizedRedirectUri}")
    private String redirectUri;
    private final TokenService tokenService;
    private static int week = 604800;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        JwtToken jwtToken = tokenService.generateToken(authentication);
        if (response.isCommitted()) {
            return;
        }
        String provider = null;
        boolean isGuest = false;
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
            provider = oauth2Token.getAuthorizedClientRegistrationId();
            Collection<GrantedAuthority> authorities = oauth2Token.getAuthorities();
            authorities.forEach(grantedAuthority -> log.info("role {}", grantedAuthority.getAuthority()));
            isGuest = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(Role.GUEST.getKey()::equals);
        }
        CookieUtil.addCookie(response, "refresh_token", jwtToken.getRefreshToken(), week);
        CookieUtil.deleteCookie(request, response, "JSESSIONID");
        String url = UriComponentsBuilder.fromHttpUrl(redirectUri)
                .queryParam("code", jwtToken.getAccessToken())
                .queryParam("provider", provider)
                .queryParam("isGuest", isGuest)
                .build()
                .toUriString();

        response.sendRedirect(url);
    }

}
