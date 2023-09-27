package com.example.waggle.service.board;

import com.example.waggle.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.component.DatabaseCleanUp;
import com.example.waggle.dto.board.answer.AnswerWriteDto;
import com.example.waggle.dto.board.question.QuestionSimpleViewDto;
import com.example.waggle.dto.board.question.QuestionViewDto;
import com.example.waggle.dto.board.question.QuestionWriteDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.service.member.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
class QuestionServiceTest {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private MemberService memberService;
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

    private void setQAndA() {
        memberService.signUp(signUpDto1);
        memberService.signUp(signUpDto2);

        questionService.saveQuestion(questionWriteDto1);
        questionService.saveQuestion(questionWriteDto2);

        List<QuestionSimpleViewDto> allQuestion = questionService.findAllQuestion();

        questionService.saveAnswer(answerWriteDto1, allQuestion.get(0).getId());
        questionService.saveAnswer(answerWriteDto2, allQuestion.get(0).getId());
    }



    @Test
    @WithMockCustomUser
    void findAllQuestion() {
        //given
        setQAndA();

        //when
        List<QuestionSimpleViewDto> allQuestion = questionService.findAllQuestion();

        //then
        assertThat(allQuestion.size()).isEqualTo(2);
    }

    @Test
    @WithMockCustomUser
    void findAllQuestionByUsername() {
        //given
        setQAndA();

        //when
        List<QuestionSimpleViewDto> allQuestionByUsername = questionService.findAllQuestionByUsername("member1");

        //then
        assertThat(allQuestionByUsername.size()).isEqualTo(2);
    }

    @Test
    @WithMockCustomUser
    void findQuestionByBoardId() {
        //given
        setQAndA();
        List<QuestionSimpleViewDto> allQuestion = questionService.findAllQuestion();
        //when
        QuestionViewDto questionByBoardId = questionService.findQuestionByBoardId(allQuestion.get(0).getId());
        //then
        assertThat(questionByBoardId.getTitle()).isEqualTo("question1");
        assertThat(questionByBoardId.getAnswers().size()).isEqualTo(2);
        assertThat(questionByBoardId.getAnswers().get(0).getContent()).isEqualTo("i don't know that");
        assertThat(questionByBoardId.getHashtags().size()).isEqualTo(2);
    }

    @Test
    @WithMockCustomUser
    void changeQuestion() {
        //given
        setQAndA();
        List<QuestionSimpleViewDto> allQuestion = questionService.findAllQuestion();
        //when
        questionService.changeQuestion(questionEditDto1, allQuestion.get(0).getId());
        QuestionViewDto questionByBoardId = questionService.findQuestionByBoardId(allQuestion.get(0).getId());
        //then
        assertThat(questionByBoardId.getTitle()).isEqualTo("EditQuestion");
        assertThat(questionByBoardId.getHashtags().size()).isEqualTo(1);
    }

    @Test
    @WithMockCustomUser
    void changeAnswer() {
        //given
        setQAndA();
        List<QuestionSimpleViewDto> allQuestion = questionService.findAllQuestion();
        QuestionViewDto findQuestion = questionService.findQuestionByBoardId(allQuestion.get(0).getId());
        //when
        questionService.changeAnswer(answerEditDto1, findQuestion.getAnswers().get(0).getId());
        QuestionViewDto questionByBoardId = questionService.findQuestionByBoardId(allQuestion.get(0).getId());
        //then
        assertThat(questionByBoardId.getAnswers().get(0).getContent()).isEqualTo("EditAnswer");

    }

    @Test
    @WithMockCustomUser
    void deleteQuestion() {
        //given
        setQAndA();
        List<QuestionSimpleViewDto> allQuestion = questionService.findAllQuestion();
        //when
        questionService.removeQuestion(allQuestion.get(0).getId());
        List<QuestionSimpleViewDto> findAllQuestion = questionService.findAllQuestion();
        //then
        assertThat(findAllQuestion.size()).isEqualTo(1);
    }

    @Test
    @WithMockCustomUser
    void deleteAnswer() {
        //given
        setQAndA();
        List<QuestionSimpleViewDto> allQuestion = questionService.findAllQuestion();
        QuestionViewDto findQuestion = questionService.findQuestionByBoardId(allQuestion.get(0).getId());
        //when
        questionService.removeAnswer(findQuestion.getAnswers().get(0).getId());
        QuestionViewDto questionByBoardId = questionService.findQuestionByBoardId(allQuestion.get(0).getId());
        //then
        assertThat(questionByBoardId.getAnswers().size()).isEqualTo(1);
    }
}