package com.example.waggle.repository.board.boardtype;

import com.example.waggle.domain.board.Story;
import com.example.waggle.domain.board.hashtag.BoardHashtag;
import com.example.waggle.domain.board.hashtag.Hashtag;
import com.example.waggle.domain.board.qna.Answer;
import com.example.waggle.domain.board.qna.Question;
import com.example.waggle.repository.board.HashtagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionRepositoryTest {

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

        Question byBoardId = questionRepository.findByBoardId(question.getId());
        assertThat(byBoardId.getAnswers().size()).isEqualTo(2);

    }

}