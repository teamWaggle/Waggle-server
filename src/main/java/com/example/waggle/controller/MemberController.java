package com.example.waggle.controller;

import com.example.waggle.component.file.FileStore;
import com.example.waggle.component.jwt.JwtToken;
import com.example.waggle.component.jwt.SecurityUtil;
import com.example.waggle.domain.member.UploadFile;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.SignInDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.service.member.MemberService;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final FileStore fileStore;

    @GetMapping("/sign-in")
    public String signInForm(Model model) {
        model.addAttribute("signInDto", new SignInDto());
        return "member/signIn";
    }

    @PostMapping("/sign-in")
    public JwtToken signIn(@ModelAttribute("signInDto") SignInDto signInDto, HttpServletResponse response) {
        JwtToken jwtToken = memberService.signIn(signInDto);
        log.info("jwtToken = {}", jwtToken.getAccessToken());

        // 로그인 성공 시
        if (jwtToken != null) {
            Cookie cookie = new Cookie("access_token", jwtToken.getAccessToken());
            cookie.setPath("/");
            cookie.setMaxAge(Integer.MAX_VALUE);
            response.addCookie(cookie);
        }

        return jwtToken;
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute("signUpDto", new SignUpDto());
        return "member/signUp";
    }

    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute("signUpDto") SignUpDto signUpDto) throws IOException {
        // 회원가입
        memberService.signUp(signUpDto);

        // 프로필 사진 설정
        UploadFile uploadFile = fileStore.storeFile(signUpDto.getProfileImg());
        if (uploadFile != null) {
            memberService.changeProfileImg(signUpDto.getUsername(), uploadFile);
        }
        return "redirect:sign-in";
    }

    @GetMapping("/sign-out")
    public String signOut(HttpServletRequest request, HttpServletResponse response) {
        log.info("sign out!");

        // HttpServletRequest에서 JWT 토큰 추출 및 쿠키 삭제
        ResponseCookie deleteCookie = ResponseCookie.from("access_token", "")
                .path("/")
                .httpOnly(true)
                .value(null)
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", deleteCookie.toString());

        // SecurityContextHolder를 초기화하여 인증 정보 제거
        SecurityContextHolder.clearContext();


        return "redirect:/";
    }

    @PostMapping("/test")
    public String test() {
        return SecurityUtil.getCurrentUsername();
    }

}
