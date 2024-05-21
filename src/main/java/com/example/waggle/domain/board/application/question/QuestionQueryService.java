package com.example.waggle.domain.board.application.question;

import com.example.waggle.domain.board.persistence.entity.Question;
import com.example.waggle.domain.board.presentation.dto.question.QuestionSortParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionQueryService {

    List<Question> getAllQuestion();

    Page<Question> getPagedQuestionsByUsername(String username, Pageable pageable);

    Page<Question> getPagedQuestionsByUserUrl(String userUrl, Pageable pageable);

    Page<Question> getPagedQuestionByMemberId(Long memberId, Pageable pageable);

    Question getQuestionByBoardId(Long boardId);

    Page<Question> getPagedQuestions(Pageable pageable);

    Page<Question> getPagedQuestionsBySortParam(QuestionSortParam sortParam, Pageable pageable);

    List<Question> getRepresentativeQuestionList();

    Page<Question> getPagedQuestionsByKeyword(String keyword, Pageable pageable);

}
