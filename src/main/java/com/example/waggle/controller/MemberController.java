package com.example.waggle.controller;

import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.member.Sex;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.PetDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.service.member.MemberService;
import com.example.waggle.service.member.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final PetService petService;

    @GetMapping("/member/sign-up")
    public String signUp() {
        log.info("MemberController");
        SignUpDto signUpDto = SignUpDto.builder()
                .username("user1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        MemberDto memberDto = memberService.signUp(signUpDto);


        PetDto petDto = PetDto.builder()
                .name("루이")
                .breed("포메라니안")
                .sex(Sex.MALE)
                .birthday(LocalDateTime.now()).build();

        petService.addPet(petDto, memberDto);

        return "OK";
    }

}
