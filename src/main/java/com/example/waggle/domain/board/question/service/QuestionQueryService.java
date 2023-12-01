package com.example.waggle.domain.board.question.service;

import com.example.waggle.web.dto.question.QuestionDetailDto;
import com.example.waggle.web.dto.question.QuestionSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionQueryService {

    List<QuestionSummaryDto> getQuestions();

    Page<QuestionSummaryDto> getPagedQuestionsByUsername(String username, Pageable pageable);

    QuestionDetailDto getQuestionByBoardId(Long boardId);

    Page<QuestionSummaryDto> getPagedQuestions(Pageable pageable);
}
