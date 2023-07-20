package com.example.waggle.component.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // HttpServletRequest에서 JWT 토큰 추출
        HttpServletRequest httpServletRequest = ((HttpServletRequest) request);
        String requestURI = httpServletRequest.getRequestURI();
        String token = Arrays.stream(httpServletRequest.getCookies())
                .filter(cookie -> cookie.getName().equals("access_token"))
                .findFirst().map(Cookie::getValue)
                .orElse("dummy");

        // 2. validateToken으로 토큰 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.setAttribute("username", authentication.getName());
            log.info("set Authentication to security context for '{}', uri: '{}'", authentication.getName(), ((HttpServletRequest) request).getRequestURI());
        } else {
            log.info("no valid JWT token found, uri: {}", ((HttpServletRequest) request).getRequestURI());
        }

        chain.doFilter(request, response);
    }

    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
