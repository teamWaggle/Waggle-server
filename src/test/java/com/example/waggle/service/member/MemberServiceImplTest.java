package com.example.waggle.service.member;

import com.example.waggle.component.DatabaseCleanUp;
import com.example.waggle.domain.member.Member;
import com.example.waggle.domain.member.Sex;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.PetDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.repository.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MemberServiceImplTest {
    @Autowired DatabaseCleanUp databaseCleanUp;
    @Autowired MemberRepository  memberRepository;
    @Autowired MemberService memberService;
    @Autowired PetService petService;

    private MemberDto savedMemberDto1;
    private MemberDto savedMemberDto2;
    private Optional<Member> findMember;

    @BeforeEach
    void beforeEach() {
        // Member 회원가입
        SignUpDto signUpDto1 = SignUpDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        SignUpDto signUpDto2 = SignUpDto.builder()
                .username("member2")
                .password("12345678")
                .nickname("닉네임2")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();

        savedMemberDto1 = memberService.signUp(signUpDto1);
        savedMemberDto2 = memberService.signUp(signUpDto2);

        findMember = memberRepository.findByUsername("member1");
    }

    @AfterEach
    void afterEach() {
        databaseCleanUp.truncateAllEntity();
    }

    @Test
    public void signUp() {
        assertThat(savedMemberDto1.getUsername()).isEqualTo(findMember.get().getUsername());
    }
}