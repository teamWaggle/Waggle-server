package com.example.waggle.board.question.repository;

import com.example.waggle.domain.board.question.entity.Answer;
import com.example.waggle.domain.board.question.repository.AnswerRepository;
import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.repository.QuestionRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AnswerRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @Transactional
    void findByQuestionIdTest() {
        Member member = Member.builder()
                .username("username")
                .password("password").build();
        Member savedMember = memberRepository.save(member);

        Question question1 = Question.builder()
                .title("question1")
                .content("i have a question sir.")
                .member(member)
                .createdDate(LocalDateTime.now())
                .createdBy("username").build();

        Answer answer1 = Answer.builder()
                .content("answer1")
                .member(member)
                .createdDate(LocalDateTime.now())
                .createdBy("username").build();

        Answer answer2 = Answer.builder()
                .content("answer2")
                .member(member)
                .createdDate(LocalDateTime.now())
                .createdBy("username").build();

        //save
        question1.addAnswer(answer1);
        question1.addAnswer(answer2);
        Question save = questionRepository.save(question1);

        //find
        List<Answer> byQuestionId = answerRepository.findByQuestionId(save.getId());
        assertThat(byQuestionId.size()).isEqualTo(2);
    }

}