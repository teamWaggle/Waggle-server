package com.example.waggle.board.question.service;


import com.example.waggle.board.question.dto.AnswerWriteDto;
import com.example.waggle.board.question.dto.QuestionDetailDto;
import com.example.waggle.board.question.dto.QuestionSummaryDto;
import com.example.waggle.board.question.dto.QuestionWriteDto;
import com.example.waggle.board.story.dto.StorySummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {
    List<QuestionSummaryDto> getQuestions();

    Page<QuestionSummaryDto> getQuestionsByUsername(String username,Pageable pageable);

    QuestionDetailDto getQuestionByBoardId(Long boardId);

    Page<QuestionSummaryDto> getQuestionsPaging(Pageable pageable);

    Long createQuestion(QuestionWriteDto questionWriteDto);

    Long createAnswer(AnswerWriteDto answerWriteDto, Long boardId);

    Long updateQuestion(Long boardId, QuestionWriteDto questionWriteDto);

    Long updateAnswer(Long boardId, AnswerWriteDto answerWriteDto);

    boolean validateMember(Long boardId, String boardType);

    void deleteQuestion(Long boardId);

    void deleteAnswer(Long boardId);
}
