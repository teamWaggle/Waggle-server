package com.example.waggle.global.security.oauth2.handler;

import com.example.waggle.domain.member.entity.Role;
import com.example.waggle.global.exception.handler.SecurityHandler;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.security.CustomUserDetails;
import com.example.waggle.global.security.JwtToken;
import com.example.waggle.global.security.TokenService;
import com.example.waggle.global.security.oauth2.cookie.CookieUtil;
import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${app.oauth2.authorizedRedirectUri}")
    private String redirectUri;
    private final TokenService tokenService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        JwtToken jwtToken = tokenService.generateToken(authentication);
        if (response.isCommitted()) {
            return;
        }
        String refreshToken = jwtToken.getRefreshToken();
        CookieUtil.addCookie(response, "refresh_token", refreshToken,60);
        CookieUtil.deleteCookie(request,response,"JSESSIONID");

        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }

}
