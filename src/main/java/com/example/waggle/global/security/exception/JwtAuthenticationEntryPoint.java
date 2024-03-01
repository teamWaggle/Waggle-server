package com.example.waggle.global.security.exception;

import com.example.waggle.global.payload.code.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        ErrorStatus errorStatus = ErrorStatus.AUTH_MUST_AUTHORIZED_URI;
        CustomErrorSend.handleException(response, errorStatus, authException.getMessage());
    }
}
