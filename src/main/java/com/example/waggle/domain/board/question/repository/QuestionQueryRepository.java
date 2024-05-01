package com.example.waggle.domain.board.question.repository;

import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.web.dto.question.QuestionFilterParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionQueryRepository {
    Page<Question> findQuestionsByFilter(QuestionFilterParam filterParam, Pageable pageable);
}
