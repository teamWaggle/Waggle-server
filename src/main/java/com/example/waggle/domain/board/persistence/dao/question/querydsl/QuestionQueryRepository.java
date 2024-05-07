package com.example.waggle.domain.board.persistence.dao.question.querydsl;

import com.example.waggle.domain.board.persistence.entity.Question;
import com.example.waggle.domain.board.presentation.dto.question.QuestionFilterParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionQueryRepository {
    Page<Question> findQuestionsByFilter(QuestionFilterParam filterParam, Pageable pageable);
}
