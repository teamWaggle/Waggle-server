package com.example.waggle.board.question.service;

import com.example.waggle.domain.board.answer.entity.Answer;
import com.example.waggle.domain.board.answer.service.AnswerCommandService;
import com.example.waggle.domain.board.answer.service.AnswerQueryService;
import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.service.QuestionCommandService;
import com.example.waggle.domain.board.question.service.QuestionQueryService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.global.component.DatabaseCleanUp;
<<<<<<< HEAD
import com.example.waggle.web.dto.answer.AnswerRequest.AnswerCreateDto;
import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import com.example.waggle.web.dto.member.MemberRequest.MemberCredentialsDto;
import com.example.waggle.web.dto.question.QuestionRequest.QuestionCreateDto;
=======
import com.example.waggle.web.dto.answer.AnswerRequest;
import com.example.waggle.web.dto.media.MediaRequest;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.question.QuestionRequest;
>>>>>>> develop
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    private MemberQueryService memberQueryService;
    @Autowired
    private AnswerCommandService answerService;
    @Autowired
    private AnswerQueryService answerQueryService;
    @Autowired
    DatabaseCleanUp databaseCleanUp;

<<<<<<< HEAD
    MemberCredentialsDto signUpDto1;
    MemberCredentialsDto signUpDto2;
=======
    MemberRequest.AccessDto signUpDto1;
    MemberRequest.AccessDto signUpDto2;
>>>>>>> develop

    QuestionCreateDto questionWriteDto1;
    QuestionCreateDto questionWriteDto2;
    QuestionCreateDto questionEditDto1;
    AnswerCreateDto answerWriteDto1;
    AnswerCreateDto answerWriteDto2;
    AnswerCreateDto answerEditDto1;


    List<String> tags1 = new ArrayList<>();
    List<String> tags2 = new ArrayList<>();

    Pageable pageable = PageRequest.of(0, 2);

    Long memberId1;
    Long memberId2;
    String member1;
    String member2;


    @BeforeEach
    void setDto() {
        tags1.add("choco");
        tags1.add("poodle");
        tags2.add("poodle");

<<<<<<< HEAD
        signUpDto1 = MemberCredentialsDto.builder()
=======
        signUpDto1 = MemberRequest.AccessDto.builder()
>>>>>>> develop
                .email("email1@naver.com")
                .password("password1")
                .build();

<<<<<<< HEAD
        signUpDto2 = MemberCredentialsDto.builder()
=======
        signUpDto2 = MemberRequest.AccessDto.builder()
>>>>>>> develop
                .email("email2@naver.com")
                .password("password2")
                .build();

        questionWriteDto1 = QuestionCreateDto.builder()
                .title("question1")
                .content("I have a question")
                .hashtagList(tags1)
                .status(Question.Status.UNRESOLVED)
                .build();

        questionWriteDto2 = QuestionCreateDto.builder()
                .title("question2")
                .content("I have a question!")
                .hashtagList(tags2)
                .status(Question.Status.UNRESOLVED)
                .build();

        questionEditDto1 = QuestionCreateDto.builder()
                .title("EditQuestion")
                .content("I wanna know that what it is")
                .hashtagList(tags2)
                .status(Question.Status.UNRESOLVED)
                .build();

        answerWriteDto1 = AnswerCreateDto.builder()
                .content("i don't know that")
                .build();

        answerWriteDto2 = AnswerCreateDto.builder()
                .content("i know that!")
                .build();

        answerEditDto1 = AnswerCreateDto.builder()
                .content("EditAnswer")
                .build();

        memberId1 = memberService.signUp(signUpDto1);
        memberId2 = memberService.signUp(signUpDto2);
        Member member = memberQueryService.getMemberById(memberId1);
        Member memberAnother = memberQueryService.getMemberById(memberId2);
        member1 = member.getUsername();
        member2 = memberAnother.getUsername();

    }

    @AfterEach
    void clean() {
        databaseCleanUp.truncateAllEntity();
    }

    private void setQAndA() {
        Member member1 = memberQueryService.getMemberById(memberId1);
        Member member2 = memberQueryService.getMemberById(memberId2);

<<<<<<< HEAD
        Long question1 = questionCommandService.createQuestionByUsername(questionWriteDto1, null,
                member1.getUsername());
        Long question2 = questionCommandService.createQuestionByUsername(questionWriteDto2, null,
                member1.getUsername());
=======
        Long question1 = questionCommandService.createQuestionByUsername(questionWriteDto1, null, member1.getUsername());
        Long question2 = questionCommandService.createQuestionByUsername(questionWriteDto2, null, member1.getUsername());
>>>>>>> develop

        answerService.createAnswerByUsername(question1, answerWriteDto1, null, member2.getUsername());
        answerService.createAnswerByUsername(question1, answerWriteDto2, null, member1.getUsername());
    }

    @Test
    void findAllQuestionByUsername() {
        //given
        setQAndA();
        //when
        Page<Question> member1 = questionQueryService
                .getPagedQuestionByMemberId(memberId1, pageable);

        //then
        assertThat(member1.getContent().size()).isEqualTo(2);
    }

    @Test
    void findQuestionByBoardId() {
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
    void changeQuestion() {
        //given
        setQAndA();
        //when
        Page<Question> pagedQuestions = questionQueryService.getPagedQuestions(pageable);
        Long aLong = questionCommandService
                .updateQuestionByUsername(pagedQuestions.getContent().get(0).getId(),
                        member1,
                        questionEditDto1,
<<<<<<< HEAD
                        new MediaUpdateDto(),
=======
                        new MediaRequest.Put(),
>>>>>>> develop
                        null);
        //then
        Question question = questionQueryService.getQuestionByBoardId(aLong);
        assertThat(question.getTitle()).isEqualTo("EditQuestion");
    }

    @Test
    void changeAnswer() {
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
    //cannot resolve -> need to add delete logic
    @Disabled
    void deleteQuestion() {
        //given
        setQAndA();
        //when
        Page<Question> pagedQuestions = questionQueryService.getPagedQuestions(pageable);
        Question question = pagedQuestions.getContent().get(0);
        questionCommandService.deleteQuestionByUsername(question.getId(), member1);
        Page<Answer> pagedAnswers = answerQueryService.getPagedAnswers(question.getId(), pageable);
        List<Question> allQuestion = questionQueryService.getAllQuestion();
        //then
        assertThat(allQuestion.size()).isEqualTo(1);
        assertThat(pagedAnswers.getContent().size()).isEqualTo(0);
    }

    @Test
    void deleteAnswer() {
        //given
        setQAndA();
        List<Question> allQuestion = questionQueryService.getAllQuestion();
        //when
        List<Answer> answers = answerQueryService.getAnswersByQuestion(allQuestion.get(0).getId());
        answerService.deleteAnswerByUsername(answers.get(0).getId(), member2);
        Page<Answer> pagedAnswers = answerQueryService.getPagedAnswers(allQuestion.get(0).getId(), pageable);
        //then
        assertThat(pagedAnswers.getContent().size()).isEqualTo(1);
    }

}