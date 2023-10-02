package com.example.waggle.member.controller;

import com.example.waggle.commons.component.file.FileStore;
import com.example.waggle.commons.security.JwtToken;
import com.example.waggle.commons.security.SecurityUtil;
import com.example.waggle.member.dto.SignInDto;
import com.example.waggle.member.dto.SignUpDto;
import com.example.waggle.commons.validation.ValidationSequence;
import com.example.waggle.commons.exception.CustomAlertException;
import com.example.waggle.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import static com.example.waggle.commons.exception.ErrorCode.MUST_WRITE_INFO_SIGN_IN;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/member")
public class MemberController {

    private final MemberService memberService;
    private final FileStore fileStore;

    @GetMapping("/sign-in")
    public String signInForm(Model model) {
        model.addAttribute("signInDto", new SignInDto());
        return "member/signIn";
    }

    @PostMapping("/sign-in")
    public String signIn(@Validated(ValidationSequence.class) @ModelAttribute("signInDto") SignInDto signInDto,
                           BindingResult bindingResult,
                           HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            throw new CustomAlertException(MUST_WRITE_INFO_SIGN_IN);
        }
        JwtToken jwtToken = memberService.signIn(signInDto);
        log.info("jwtToken = {}", jwtToken.getAccessToken());

        // 로그인 성공 시
        if (jwtToken != null) {
            Cookie cookie = new Cookie("access_token", jwtToken.getAccessToken());
            cookie.setPath("/");
            cookie.setMaxAge(Integer.MAX_VALUE);
            response.addCookie(cookie);
        }

        return "redirect:/";
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute("signUpDto", new SignUpDto());
        return "member/signUp";
    }

    @PostMapping("/sign-up")
    public String signUp(@Validated(ValidationSequence.class) @ModelAttribute("signUpDto") SignUpDto signUpDto,
                         BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "member/signUp";
            //throw new CustomAlertException(MUST_WRITE_INFO_SIGN_UP);
        }
        memberService.signUp(signUpDto, null);
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
