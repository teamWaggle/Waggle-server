package com.example.waggle.service.member;

import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.member.Sex;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.PetDto;
import com.example.waggle.dto.member.SignUpDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MemberServiceImplTest {

    @Autowired private MemberService memberService;
    @Autowired private PetService petService;

    @Test
    @Transactional
    public void signUp() {
        SignUpDto signUpDto = SignUpDto.builder()
                .username("user1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        MemberDto savedMemberDto = memberService.signUp(signUpDto);

        PetDto petDto = PetDto.builder()
                .name("루이")
                .breed("포메라니안")
                .sex(Sex.MALE)
                .birthday(LocalDateTime.now()).build();

        PetDto savedPetDto = petService.addPet(petDto, savedMemberDto.getId());

        assertThat(savedMemberDto.getPets().get(0)).usingRecursiveComparison().isEqualTo(savedPetDto);
    }
}