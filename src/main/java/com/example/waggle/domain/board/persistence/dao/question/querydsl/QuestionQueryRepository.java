package com.example.waggle.domain.board.persistence.dao.question.querydsl;

import com.example.waggle.domain.board.persistence.entity.Question;
import com.example.waggle.domain.board.presentation.dto.question.QuestionSortParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionQueryRepository {
    Page<Question> findQuestionsBySortParam(QuestionSortParam sortParam, Pageable pageable);

    Page<Question> findQuestionsByKeyword(String keyword, Pageable pageable);

    Page<Question> findQuestionListByKeywordAndSortParam(String keyword, QuestionSortParam sortParam, Pageable pageable);

}
