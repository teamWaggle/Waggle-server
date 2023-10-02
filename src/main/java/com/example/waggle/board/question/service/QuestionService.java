package com.example.waggle.board.question.service;


import com.example.waggle.board.question.dto.AnswerWriteDto;
import com.example.waggle.board.question.dto.QuestionSummaryDto;
import com.example.waggle.board.question.dto.QuestionDetailDto;
import com.example.waggle.board.question.dto.QuestionWriteDto;

import java.util.List;

public interface QuestionService {
    List<QuestionSummaryDto> getQuestions();

    List<QuestionSummaryDto> getQuestionsByUsername(String username);

    QuestionDetailDto getQuestionByBoardId(Long boardId);

    Long createQuestion(QuestionWriteDto questionWriteDto);

    Long createAnswer(AnswerWriteDto answerWriteDto, Long boardId);

    Long updateQuestion(QuestionWriteDto questionWriteDto, Long boardId);

    Long updateAnswer(AnswerWriteDto answerWriteDto, Long boardId);

    boolean validateMember(Long boardId, String boardType);

    void deleteQuestion(Long boardId);

    void deleteAnswer(Long boardId);
}
