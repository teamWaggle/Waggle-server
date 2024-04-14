package com.example.waggle.global.security.oauth2.handler;

import com.example.waggle.global.security.object.JwtToken;
import com.example.waggle.global.security.service.TokenService;
import com.example.waggle.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;


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
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
            provider = oauth2Token.getAuthorizedClientRegistrationId();
        }
        CookieUtil.addCookie(response, "refresh_token", jwtToken.getRefreshToken(), week);
        CookieUtil.deleteCookie(request, response, "JSESSIONID");
        String url = UriComponentsBuilder.fromHttpUrl(redirectUri)
                .queryParam("code", jwtToken.getAccessToken())
                .queryParam("provider", provider)
                .build()
                .toUriString();

        response.sendRedirect(url);
    }

}
