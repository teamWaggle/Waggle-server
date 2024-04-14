package com.example.waggle.global.security.oauth2.handler;

import com.example.waggle.global.exception.filter.OAuth2Exception;
import com.example.waggle.global.payload.code.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${app.oauth2.authorizedRedirectUri}")
    private String redirectUri;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        ErrorStatus errorStatus = ErrorStatus._INTERNAL_SERVER_ERROR;
        if (exception instanceof OAuth2Exception) {
            OAuth2AuthenticationException oAuth2Exception = (OAuth2AuthenticationException) exception;
            OAuth2Error error = oAuth2Exception.getError();
            errorStatus = ErrorStatus.valueOf(error.getErrorCode());
        }
        String errorUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("error", errorStatus.getCode())
                .build().toUriString();
        response.sendRedirect(errorUrl);
    }
}
