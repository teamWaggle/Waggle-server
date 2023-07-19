package com.example.waggle.controller;

import com.example.waggle.component.jwt.JwtToken;
import com.example.waggle.component.jwt.SecurityUtil;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.SignInDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/sign-in")
    public String signInForm(Model model) {
        model.addAttribute("signInDto", new SignInDto());
        return "member/signIn";
    }

    @PostMapping("/sign-in")
    public String signIn(@ModelAttribute("signInDto") SignInDto signInDto) {
        log.info("signInDto = {}", signInDto);
        JwtToken jwtToken = memberService.signIn(signInDto);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return "redirect:/";
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute("signUpDto", new SignUpDto());
        return "member/signUp";
    }

    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute("signUpDto") SignUpDto signUpDto) {
        MemberDto signUpMemberDto = memberService.signUp(signUpDto);
        return "redirect:sign-in";
    }


    @PostMapping("/test")
    @ResponseBody
    public String test() {
        return SecurityUtil.getCurrentUsername();
    }
    @GetMapping("/test")
    @ResponseBody
    public String test2() {
        return SecurityUtil.getCurrentUsername();
    }


}
