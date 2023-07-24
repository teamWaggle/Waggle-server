package com.example.waggle.service.board;

import com.example.waggle.dto.board.QuestionDto;
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
    private QuestionDto questionDto1;
    private QuestionDto questionDto2;


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
        questionDto1 = QuestionDto.builder()
                .content("question1. i wanna asnwer!")
                .title("my dog is cooool")
                .username("member1")
                .build();

    }

    //signUp x2 -> board writing -> signIn another
    // -> recommend question -> result check
    @Test
    @Rollback(value = false)
    void recommendQuestion() {
        //signUp
        memberService.signUp(signUpDto1);

        //board writing
        Long questionId = questionService.saveQuestion("member1", questionDto1);

        //recommend question
        recommendService.clickRecommend(questionId, "member1", "question");
    }

}