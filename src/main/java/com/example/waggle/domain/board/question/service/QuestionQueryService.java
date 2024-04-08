package com.example.waggle.domain.board.question.service;

import com.example.waggle.domain.board.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionQueryService {

    List<Question> getAllQuestion();

    Page<Question> getPagedQuestionsByUsername(String username, Pageable pageable);

    Page<Question> getPagedQuestionByMemberId(Long memberId, Pageable pageable);

    Question getQuestionByBoardId(Long boardId);

    Page<Question> getPagedQuestions(Pageable pageable);

    List<Question> getRepresentativeQuestionList();

    Integer getViewCountInRedis(Long boardId);

}
