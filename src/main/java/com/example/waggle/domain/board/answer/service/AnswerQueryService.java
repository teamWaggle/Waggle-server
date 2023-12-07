package com.example.waggle.domain.board.answer.service;

import com.example.waggle.domain.board.answer.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnswerQueryService {

    Page<Answer> getPagedAnswerByUsername(String username, Pageable pageable);

    Answer getAnswerByBoardId(Long boardId);
    Page<Answer> getPagedAnswers(Long questionId, Pageable pageable);

    List<Answer> getAnswersByQuestion(Long questionId);

}
