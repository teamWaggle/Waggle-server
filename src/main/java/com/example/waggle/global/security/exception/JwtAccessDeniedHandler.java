package com.example.waggle.global.security.exception;

import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("denied");
//        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        ErrorStatus errorStatus = ErrorStatus.AUTH_ROLE_CANNOT_EXECUTE_URI;
        ApiResponseDto<Object> apiResponseEntity = ApiResponseDto.onFailure(errorStatus.getCode(), errorStatus.getMessage(), accessDeniedException.getMessage());

        response.setStatus(errorStatus.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponseEntity));
    }
}
