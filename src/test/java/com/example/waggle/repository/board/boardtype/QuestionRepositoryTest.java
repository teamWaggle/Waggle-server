package com.example.waggle.repository.board.boardtype;

import com.example.waggle.domain.board.boardType.Answer;
import com.example.waggle.domain.board.boardType.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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

        Optional<Question> byBoardId = questionRepository.findById(question.getId());
        assertThat(byBoardId.get().getAnswers().size()).isEqualTo(2);

    }

}