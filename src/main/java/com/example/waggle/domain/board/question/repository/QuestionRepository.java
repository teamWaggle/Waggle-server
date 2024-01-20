package com.example.waggle.domain.board.question.repository;

import com.example.waggle.domain.board.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findListByMemberUsername(String username);

    List<Question> findAllByOrderByCreatedDateDesc();

    Page<Question> findAll(Pageable pageable);

    Page<Question> findByMemberUsername(String username, Pageable pageable);

    void deleteAllByMemberUsername(String username);

}