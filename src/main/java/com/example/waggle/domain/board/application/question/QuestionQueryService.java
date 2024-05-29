package com.example.waggle.domain.board.application.question;

import com.example.waggle.domain.board.persistence.entity.Question;
import com.example.waggle.domain.board.presentation.dto.question.QuestionSortParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionQueryService {

    List<Question> getAllQuestion();

    Page<Question> getPagedQuestionListByUsername(String username, Pageable pageable);

    Page<Question> getPagedQuestionListByUserUrl(String userUrl, Pageable pageable);

    Page<Question> getPagedQuestionListByMemberId(Long memberId, Pageable pageable);

    Question getQuestionByBoardId(Long boardId);

    Page<Question> getPagedQuestionList(Pageable pageable);

    Page<Question> getPagedQuestionListBySortParam(QuestionSortParam sortParam, Pageable pageable);

    List<Question> getRepresentativeQuestionList();

    Page<Question> getPagedQuestionListByKeyword(String keyword, Pageable pageable);

    Page<Question> getPagedQuestionListByKeywordAndSortParam(String keyword, QuestionSortParam sortParam, Pageable pageable);

}
