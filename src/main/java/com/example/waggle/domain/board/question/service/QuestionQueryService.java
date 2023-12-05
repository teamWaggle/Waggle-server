package com.example.waggle.domain.board.question.service;

import com.example.waggle.domain.board.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionQueryService {

//    List<Question> getQuestions();

    Page<Question> getPagedQuestionsByUsername(String username, Pageable pageable);

    Question getQuestionByBoardId(Long boardId);

    Page<Question> getPagedQuestions(Pageable pageable);
}
