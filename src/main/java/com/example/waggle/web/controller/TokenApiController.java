package com.example.waggle.web.controller;

import com.example.waggle.global.annotation.ApiErrorCodeExample;
import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.security.object.CookieStatus;
import com.example.waggle.global.security.object.JwtToken;
import com.example.waggle.global.security.service.TokenService;
import com.example.waggle.global.util.CookieUtil;
import com.example.waggle.web.dto.member.MemberRequest.MemberCredentialsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/tokens")
@RestController
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Token API", description = "토큰 API")
public class TokenApiController {

    private final TokenService tokenService;
    private static int week = 604800;


    @Operation(summary = "로그인", description = "아이디와 비밀번호를 통해 로그인을 진행합니다. 성공 시 엑세스 토큰을 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping
    public ApiResponseDto<JwtToken> login(@RequestBody MemberCredentialsDto loginRequest,
                                          HttpServletResponse response) {
        JwtToken jwtToken = tokenService.login(loginRequest);
        CookieUtil.addCookie(response, "refresh_token", jwtToken.getRefreshToken(), week);
        return ApiResponseDto.onSuccess(jwtToken);
    }


    @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 재발급합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping("/refresh")
    public ApiResponseDto<JwtToken> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshInCookie(request, response);
        JwtToken newTokens = tokenService.issueTokens(refreshToken);
        CookieUtil.addCookie(response, "refresh_token", newTokens.getRefreshToken(), week);
        return ApiResponseDto.onSuccess(newTokens);
    }

    @Operation(summary = "로그아웃", description = "로그아웃을 진행합니다. 현재 사용자의 엑세스 토큰을 무효화하고 인증 정보를 제거합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping
    public ApiResponseDto<CookieStatus> logout(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> refreshToken = CookieUtil.getCookie(request, "refresh_token")
                .map(cookie -> cookie.getValue());
        refreshToken.ifPresent(token -> removeRefreshInRedis(request, response, token));
        CookieStatus cookieStatus = refreshToken.isPresent() ? CookieStatus.EXIST_REFRESH : CookieStatus.NOT_EXIST_REFRESH;
        return ApiResponseDto.onSuccess(cookieStatus);
    }

    private static String getRefreshInCookie(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtil.getCookie(request, "refresh_token")
                .map(cookie -> cookie.getValue())
                .orElseThrow(() -> new GeneralException(ErrorStatus.AUTH_REFRESH_NOT_EXIST_IN_COOKIE));
        CookieUtil.deleteCookie(request, response, "refresh_token");
        return refreshToken;
    }

    private void removeRefreshInRedis(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        if (!tokenService.existsRefreshToken(refreshToken)) {
            throw new GeneralException(ErrorStatus.AUTH_INVALID_REFRESH_TOKEN);
        } else {
            tokenService.logout(refreshToken);
        }
        CookieUtil.deleteCookie(request, response, "refresh_token");
    }
}
