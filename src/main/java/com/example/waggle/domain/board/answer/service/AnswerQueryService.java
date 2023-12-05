package com.example.waggle.domain.board.answer.service;

import com.example.waggle.domain.board.answer.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnswerQueryService {

    Page<Answer> getPagedAnswerByUsername(String username, Pageable pageable);

    Answer getAnswerByBoardId(Long boardId);
    Page<Answer> getPagedAnswers(Long questionId, Pageable pageable);

}
