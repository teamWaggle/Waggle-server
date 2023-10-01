package com.example.waggle.board.question.repository;

import com.example.waggle.board.question.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByMemberUsername(String username);

}