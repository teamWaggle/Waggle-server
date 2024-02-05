package com.example.waggle.global.security.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${app.oauth2.authorizedRedirectUri}")
    private String redirectUri;
//    private final TokenService tokenService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
//        JwtToken jwtToken = tokenService.generateToken(authentication);
//        if (response.isCommitted()) {
//            return;
//        }
//        String refreshToken = jwtToken.getRefreshToken();
//        CookieUtil.addCookie(response, "refresh_token", refreshToken, 60);
//        CookieUtil.deleteCookie(request, response, "JSESSIONID");
//
//        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }

}
