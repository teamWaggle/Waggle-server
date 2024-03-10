package com.example.waggle.global.security.oauth2.handler;

import com.example.waggle.global.exception.filter.OAuth2Exception;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.security.exception.CustomErrorSend;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${app.oauth2.authorizedRedirectUri}")
    private String redirectUri;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        if (exception instanceof OAuth2Exception) {
            OAuth2AuthenticationException oAuth2Exception = (OAuth2AuthenticationException) exception;
            OAuth2Error error = oAuth2Exception.getError();
            ErrorStatus errorStatus = ErrorStatus.valueOf(error.getErrorCode());
            CustomErrorSend.handleException(response, errorStatus, error.getErrorCode());
        } else {
            String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                    .queryParam("error", exception.getLocalizedMessage())
                    .build().toUriString();
            targetUrl = UriUtils.encode(targetUrl, "UTF-8");

            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
    }
}
