package com.example.waggle.service.board;

import com.example.waggle.dto.board.question.QuestionViewDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QuestionServiceTest {

    @Autowired
    QuestionService questionService;
    @Autowired
    MemberService memberService;
    @Autowired
    RecommendService recommendService;

    private SignUpDto signUpDto1;
    private SignUpDto signUpDto2;
    private QuestionViewDto questionDto1;
    private QuestionViewDto questionDto2;


    @BeforeEach
    void beforeEach() {
        //member
        signUpDto1 = SignUpDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        signUpDto2 = SignUpDto.builder()
                .username("member2")
                .password("12345678")
                .nickname("닉네임2")
                .address("서울시 광진구")
                .phone("010-1234-5678")
                .build();
        //board(new build)
        questionDto1 = QuestionViewDto.builder()
                .content("question1. i wanna asnwer!")
                .title("my dog is cooool")
                .username("member1")
                .build();

    }


    @Test
    @Rollback(value = false)
    void recommendQuestion() {

    }

}