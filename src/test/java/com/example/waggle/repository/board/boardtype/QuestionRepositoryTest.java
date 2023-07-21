package com.example.waggle.repository.board.boardtype;

import com.example.waggle.domain.board.Story;
import com.example.waggle.domain.board.hashtag.BoardHashtag;
import com.example.waggle.domain.board.hashtag.Hashtag;
import com.example.waggle.domain.board.qna.Answer;
import com.example.waggle.domain.board.qna.Question;
import com.example.waggle.domain.member.Member;
import com.example.waggle.repository.board.HashtagRepository;
import com.example.waggle.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
    @Rollback(value = false)
    void test() {
        Question question = Question.builder().title("question").content("i have a question sir.").build();
        questionRepository.save(question);

        question.addAnswer(Answer.builder().content("what is it?").build());
        question.addAnswer(Answer.builder().content("what is it???").build());

        Optional<Question> byBoardId = questionRepository.findById(question.getId());
        assertThat(byBoardId.get().getAnswers().size()).isEqualTo(2);

    }

    @Test
    @Transactional
    @Rollback(false)
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

        savedQuestion1.addAnswer(answer1);
        savedQuestion1.addAnswer(answer2);
        savedQuestion2.addAnswer(answer3);

        // then
        List<Question> questionList = questionRepository.findByMemberUsername(savedMember.getUsername());

        assertThat(questionList.size()).isEqualTo(2);
        for (Question questionCheck : questionList) {
            log.info("question = {}", questionCheck.getContent());
        }
    }

}