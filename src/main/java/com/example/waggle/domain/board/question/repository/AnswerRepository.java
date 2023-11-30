package com.example.waggle.domain.board.question.repository;

import com.example.waggle.domain.board.question.domain.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionId(Long questionId);
    List<Answer> findAllByOrderByCreatedDateAsc();
    Page<Answer> findAll(Pageable pageable);
}