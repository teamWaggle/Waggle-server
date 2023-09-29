package com.example.waggle.repository.board.boardtype;

import com.example.waggle.domain.board.boardType.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByMemberUsername(String username);

}