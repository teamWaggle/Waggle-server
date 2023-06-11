package com.example.waggle.repository.board.boardtype;

import com.example.waggle.domain.board.qna.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByQuestionId(@Param("questionId")Long id);
}