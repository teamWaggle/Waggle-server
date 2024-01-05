package com.example.waggle.board.question.service;

import com.example.waggle.domain.board.answer.entity.Answer;
import com.example.waggle.domain.board.answer.service.AnswerCommandService;
import com.example.waggle.domain.board.answer.service.AnswerQueryService;
import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.service.QuestionCommandService;
import com.example.waggle.domain.board.question.service.QuestionQueryService;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.answer.AnswerRequest;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.web.dto.media.MediaRequest;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.question.QuestionRequest;
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
    QuestionCommandService questionCommandService;
    @Autowired
    private QuestionQueryService questionQueryService;
    @Autowired
    private MemberCommandService memberService;
    @Autowired
    private AnswerCommandService answerService;
    @Autowired
    private AnswerQueryService answerQueryService;
    @Autowired
    DatabaseCleanUp databaseCleanUp;

    MemberRequest.RegisterDto signUpDto1;
    MemberRequest.RegisterDto signUpDto2;

    QuestionRequest.Post questionWriteDto1;
    QuestionRequest.Post questionWriteDto2;
    QuestionRequest.Put questionEditDto1;
    AnswerRequest.Post answerWriteDto1;
    AnswerRequest.Post answerWriteDto2;
    AnswerRequest.Post answerEditDto1;


    List<String> tags1 = new ArrayList<>();
    List<String> tags2 = new ArrayList<>();

    Pageable pageable = PageRequest.of(0, 2);


    @BeforeEach
    void setDto() {
        tags1.add("choco");
        tags1.add("poodle");
        tags2.add("poodle");

        signUpDto1 = MemberRequest.RegisterDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .email("a;sldkfj")
                .phone("010-1234-5678")
                .build();

        signUpDto2 = MemberRequest.RegisterDto.builder()
                .username("member2")
                .password("12345678")
                .nickname("닉네임2")
                .address("서울시 광진구")
                .email("euufhdjhf")
                .phone("010-1234-5678")
                .build();

        questionWriteDto1 = QuestionRequest.Post.builder()
                .title("question1")
                .content("I have a question")
                .hashtags(tags1)
                .status(Question.Status.UNRESOLVED)
                .build();


        questionWriteDto2 = QuestionRequest.Post.builder()
                .title("question2")
                .content("I have a question!")
                .hashtags(tags2)
                .status(Question.Status.UNRESOLVED)
                .build();


        questionEditDto1 = QuestionRequest.Put.builder()
                .title("EditQuestion")
                .content("I wanna know that what it is")
                .hashtags(tags2)
                .username("member1")
                .status(Question.Status.UNRESOLVED)
                .build();

        answerWriteDto1 = AnswerRequest.Post.builder()
                .content("i don't know that")
                .build();

        answerWriteDto2 = AnswerRequest.Post.builder()
                .content("i know that!")
                .build();

        answerEditDto1 = AnswerRequest.Post.builder()
                .content("EditAnswer")
                .build();

    }

    @AfterEach
    void clean() {
        databaseCleanUp.truncateAllEntity();
    }

    private void setQAndA() throws IOException {
        memberService.signUp(signUpDto1);
        memberService.signUp(signUpDto2);

        Long question1 = questionCommandService.createQuestion(questionWriteDto1, null);
        Long question2 = questionCommandService.createQuestion(questionWriteDto2, null);

        answerService.createAnswer(question1, answerWriteDto1, null);
        answerService.createAnswer(question1, answerWriteDto2, null);
    }

    @Test
    @WithMockCustomUser
    void findAllQuestionByUsername() throws IOException {
        //given
        setQAndA();
        //when
        Page<Question> member1 = questionQueryService
                .getPagedQuestionsByUsername("member1", pageable);

        //then
        assertThat(member1.getContent().size()).isEqualTo(2);
    }

    @Test
    @WithMockCustomUser
    void findQuestionByBoardId() throws IOException {
        //given
        setQAndA();
        //when
        Page<Question> pagedQuestions = questionQueryService.getPagedQuestions(pageable);
        Question questionByBoardId = questionQueryService
                .getQuestionByBoardId(pagedQuestions.getContent().get(0).getId());
        //then
        assertThat(questionByBoardId.getTitle()).isEqualTo("question1");
        assertThat(pagedQuestions.getSize()).isEqualTo(2);
    }

    @Test
    @WithMockCustomUser
    void changeQuestion() throws IOException {
        //given
        setQAndA();
        //when
        Page<Question> pagedQuestions = questionQueryService.getPagedQuestions(pageable);
        Long aLong = questionCommandService
                .updateQuestionV2(pagedQuestions.getContent().get(0).getId(), questionEditDto1, new MediaRequest.Put(), null);
        //then
        Question question = questionQueryService.getQuestionByBoardId(aLong);
        assertThat(question.getTitle()).isEqualTo("EditQuestion");
    }

    @Test
    @WithMockCustomUser
    void changeAnswer() throws IOException {
        //given
        setQAndA();
        Page<Question> pagedQuestions = questionQueryService.getPagedQuestions(pageable);
        Question question = pagedQuestions.getContent().get(0);
        //when
        Page<Answer> pagedAnswers = answerQueryService.getPagedAnswers(question.getId(), pageable);
        Long aLong = answerService.updateAnswer(pagedAnswers.getContent().get(0).getId(), answerEditDto1, null, null);
        Answer answerByBoardId = answerQueryService.getAnswerByBoardId(aLong);
        //then
        assertThat(answerByBoardId.getContent()).isEqualTo("EditAnswer");
    }

    @Test
    @WithMockCustomUser
    void deleteQuestion() throws IOException {
        //given
        setQAndA();
        //when
        Page<Question> pagedQuestions = questionQueryService.getPagedQuestions(pageable);
        Question question = pagedQuestions.getContent().get(0);
        questionCommandService.deleteQuestion(question.getId());
        Page<Answer> pagedAnswers = answerQueryService.getPagedAnswers(question.getId(), pageable);
        List<Question> allQuestion = questionQueryService.getAllQuestion();
        //then
        assertThat(allQuestion.size()).isEqualTo(1);
        assertThat(pagedAnswers.getContent().size()).isEqualTo(0);
    }

    @Test
    @WithMockCustomUser
    void deleteAnswer() throws IOException {
        //given
        setQAndA();
        List<Question> allQuestion = questionQueryService.getAllQuestion();
        //when
        List<Answer> answers = answerQueryService.getAnswersByQuestion(allQuestion.get(0).getId());
        answerService.deleteAnswer(answers.get(0).getId());
        Page<Answer> pagedAnswers = answerQueryService.getPagedAnswers(allQuestion.get(0).getId(), pageable);
        //then
        assertThat(pagedAnswers.getContent().size()).isEqualTo(1);
    }

}