package com.example.waggle.board.question.repository;


import com.example.waggle.domain.board.answer.entity.Answer;
import com.example.waggle.domain.board.answer.repository.AnswerRepository;
import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.repository.QuestionRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class QuestionRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    AnswerRepository answerRepository;

    @Test
    @Transactional
    void test() {
        Question question = Question.builder().title("question").content("i have a question sir.").build();
        questionRepository.save(question);



        Optional<Question> byBoardId = questionRepository.findById(question.getId());

    }

    @Test
    @Transactional
    void findByMemberUsernameTest() {
        // given
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

        Question question2 = Question.builder()
                .title("question2")
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

        Answer answer3 = Answer.builder()
                .content("answer3")
                .member(member)
                .createdDate(LocalDateTime.now())
                .createdBy("username").build();

        // when
        Question savedQuestion1 = questionRepository.save(question1);
        Question savedQuestion2 = questionRepository.save(question2);


        answerRepository.save(answer1);
        answerRepository.save(answer2);
        answerRepository.save(answer3);



        // then
        Pageable pageable = PageRequest.of(0, 3);
        Page<Question> questionByUsername = questionRepository
                .findByMemberUsername(savedMember.getUsername(), pageable);

        assertThat(questionByUsername.getContent().size()).isEqualTo(2);
        for (Question questionCheck : questionByUsername.getContent()) {
            log.info("question = {}", questionCheck.getContent());
        }
    }

}