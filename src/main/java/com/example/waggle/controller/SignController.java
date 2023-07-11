package com.example.waggle.controller;

import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.SignInDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("member")
public class SignController {

    private final MemberService memberService;

    /**
     * Sign In
     */
    @GetMapping("/signIn")
    public String signIn(@ModelAttribute("signInDto") SignInDto signInDto) {
        return "private/signIn";
    }

    @PostMapping("/signIn")
    public String signInPost(@ModelAttribute("signInDto") SignInDto signInDto) {
        memberService.signIn(signInDto);
        return "redirect:main";
    }

    /**
     * Sign Up
     */
    @GetMapping("/signUp")
    public String signUp(@ModelAttribute("signUpDto") SignUpDto signUpDto) {
        return "/private/signUp";
    }

    @PostMapping("/signUp")
    public String signUpPost(@ModelAttribute("signUpDto") SignUpDto signUpDto) {
        memberService.signUp(signUpDto);
        return "redirect:member/signIn";
    }

    /**
     * Sign Out
     */
}
