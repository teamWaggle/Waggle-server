package com.example.waggle.domain.board.answer.repository;

import com.example.waggle.domain.board.answer.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Page<Answer> findByQuestionId(Long questionId,Pageable pageable);
    Page<Answer> findAll(Pageable pageable);

    Page<Answer> findPagedAnswerByMemberUsername(String username, Pageable pageable);
}