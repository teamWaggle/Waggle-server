package com.example.waggle.domain.board.application.question;

import com.example.waggle.domain.board.persistence.entity.Question;
import com.example.waggle.domain.board.presentation.dto.question.QuestionSortParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionQueryService {

    Page<Question> getPagedQuestionListByUserUrl(String userUrl, Pageable pageable);

    Question getQuestionByBoardId(Long boardId);

    List<Question> getRepresentativeQuestionList();

    Page<Question> getPagedQuestionListByKeywordAndSortParam(String keyword, QuestionSortParam sortParam, Pageable pageable);

}
