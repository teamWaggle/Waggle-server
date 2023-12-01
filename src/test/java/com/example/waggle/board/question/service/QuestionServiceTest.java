package com.example.waggle.board.question.service;

import com.example.waggle.domain.board.question.service.QuestionCommandService;
import com.example.waggle.domain.board.question.service.QuestionQueryService;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.answer.AnswerWriteDto;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.web.dto.member.SignUpDto;
import com.example.waggle.web.dto.question.QuestionDetailDto;
import com.example.waggle.web.dto.question.QuestionSummaryDto;
import com.example.waggle.web.dto.question.QuestionWriteDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QuestionServiceTest {

    @Autowired
    private QuestionCommandService questionService;
    @Autowired
    private QuestionQueryService questionQueryService;
    @Autowired
    private MemberCommandService memberService;
    @Autowired
    DatabaseCleanUp databaseCleanUp;

    SignUpDto signUpDto1;
    SignUpDto signUpDto2;

    QuestionWriteDto questionWriteDto1;
    QuestionWriteDto questionWriteDto2;
    QuestionWriteDto questionEditDto1;
    AnswerWriteDto answerWriteDto1;
    AnswerWriteDto answerWriteDto2;
    AnswerWriteDto answerEditDto1;


    List<String> tags1 = new ArrayList<>();
    List<String> tags2 = new ArrayList<>();
    List<String> medias1 = new ArrayList<>();
    List<String> medias2 = new ArrayList<>();


    @BeforeEach
    void setDto() {
        tags1.add("choco");
        tags1.add("poodle");
        tags2.add("poodle");

        medias1.add("media1");
        medias1.add("mediamedia1");
        medias2.add("media2");
        medias2.add("mediamedia2");

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

        questionWriteDto1 = QuestionWriteDto.builder()
                .title("question1")
                .content("I have a question")
                .medias(medias1)
                .hashtags(tags1)
                .build();


        questionWriteDto2 = QuestionWriteDto.builder()
                .title("question2")
                .content("I have a question!")
                .medias(medias2)
                .hashtags(tags2)
                .build();

        questionEditDto1 = QuestionWriteDto.builder()
                .title("EditQuestion")
                .content("I wanna know that what it is")
                .medias(medias2)
                .hashtags(tags2)
                .build();

        answerWriteDto1 = AnswerWriteDto.builder()
                .content("i don't know that")
                .build();

        answerWriteDto2 = AnswerWriteDto.builder()
                .content("i know that!")
                .build();

        answerEditDto1 = AnswerWriteDto.builder()
                .content("EditAnswer")
                .build();

    }

    @AfterEach
    void clean() {
        databaseCleanUp.truncateAllEntity();
    }

    private void setQAndA() throws IOException {
        memberService.signUp(signUpDto1, null);
        memberService.signUp(signUpDto2, null);

        Long question1 = questionService.createQuestion(questionWriteDto1, new ArrayList<>());
        Long question2 = questionService.createQuestion(questionWriteDto2, new ArrayList<>());

        questionService.createAnswer(question1, answerWriteDto1, new ArrayList<>());
        questionService.createAnswer(question1, answerWriteDto2, new ArrayList<>());
    }


    @WithMockCustomUser
    @Test
    void findAllQuestion() throws IOException {
        //given
        setQAndA();

        //when
        List<QuestionSummaryDto> allQuestion = questionQueryService.getQuestions();

        //then
        assertThat(allQuestion.size()).isEqualTo(2);
    }

    @Test
    @WithMockCustomUser
    void findAllQuestionByUsername() throws IOException {
        //given
        setQAndA();

        //when
        Pageable pageable = PageRequest.of(0, 2);
        Page<QuestionSummaryDto> questionsByUsername = questionQueryService
                .getPagedQuestionsByUsername("member1", pageable);

        //then
        assertThat(questionsByUsername.getContent().size()).isEqualTo(2);
    }

    @Test
    @WithMockCustomUser
    void findQuestionByBoardId() throws IOException {
        //given
        setQAndA();
        List<QuestionSummaryDto> allQuestion = questionQueryService.getQuestions();
        //when
        QuestionDetailDto questionByBoardId = questionQueryService.getQuestionByBoardId(allQuestion.get(0).getId());
        //then
        assertThat(questionByBoardId.getTitle()).isEqualTo("question1");
        assertThat(questionByBoardId.getAnswers().size()).isEqualTo(2);
        assertThat(questionByBoardId.getAnswers().get(0).getContent()).isEqualTo("i don't know that");
        assertThat(questionByBoardId.getHashtags().size()).isEqualTo(2);
    }

    @Test
    @WithMockCustomUser
    void changeQuestion() throws IOException {
        //given
        setQAndA();

        List<QuestionSummaryDto> allQuestion = questionQueryService.getQuestions();
        //when
        questionService.updateQuestion(allQuestion.get(0).getId(), questionEditDto1, new ArrayList<>());
        QuestionDetailDto questionByBoardId = questionQueryService.getQuestionByBoardId(allQuestion.get(0).getId());
        //then
        assertThat(questionByBoardId.getTitle()).isEqualTo("EditQuestion");
        assertThat(questionByBoardId.getHashtags().size()).isEqualTo(1);
    }

    @Test
    @WithMockCustomUser
    void changeAnswer() throws IOException {
        //given
        setQAndA();
        List<QuestionSummaryDto> allQuestion = questionQueryService.getQuestions();
        QuestionDetailDto findQuestion = questionQueryService.getQuestionByBoardId(allQuestion.get(0).getId());
        //when
        questionService.updateAnswer(findQuestion.getAnswers().get(0).getId(), answerEditDto1, new ArrayList<>());
        QuestionDetailDto questionByBoardId = questionQueryService.getQuestionByBoardId(allQuestion.get(0).getId());
        //then
        assertThat(questionByBoardId.getAnswers().get(0).getContent()).isEqualTo("EditAnswer");

    }

    @Test
    @WithMockCustomUser
    void deleteQuestion() throws IOException {
        //given
        setQAndA();
        List<QuestionSummaryDto> allQuestion = questionQueryService.getQuestions();
        //when
        questionService.deleteQuestion(allQuestion.get(0).getId());
        List<QuestionSummaryDto> findAllQuestion = questionQueryService.getQuestions();
        //then
        assertThat(findAllQuestion.size()).isEqualTo(1);
    }

    @Test
    @WithMockCustomUser
    void deleteAnswer() throws IOException {
        //given
        setQAndA();
        List<QuestionSummaryDto> allQuestion = questionQueryService.getQuestions();
        QuestionDetailDto findQuestion = questionQueryService.getQuestionByBoardId(allQuestion.get(0).getId());
        //when
        questionService.deleteAnswer(findQuestion.getAnswers().get(0).getId());
        QuestionDetailDto questionByBoardId = questionQueryService.getQuestionByBoardId(allQuestion.get(0).getId());
        //then
        assertThat(questionByBoardId.getAnswers().size()).isEqualTo(1);
    }
}