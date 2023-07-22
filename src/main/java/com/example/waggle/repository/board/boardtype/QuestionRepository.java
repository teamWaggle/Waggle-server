package com.example.waggle.repository.board.boardtype;

import com.example.waggle.domain.board.Story;
import com.example.waggle.domain.board.qna.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByMemberUsername(String username);

}