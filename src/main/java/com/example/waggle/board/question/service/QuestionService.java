package com.example.waggle.board.question.service;


import com.example.waggle.board.question.dto.AnswerWriteDto;
import com.example.waggle.board.question.dto.QuestionSimpleViewDto;
import com.example.waggle.board.question.dto.QuestionViewDto;
import com.example.waggle.board.question.dto.QuestionWriteDto;

import java.util.List;

public interface QuestionService {
    public List<QuestionSimpleViewDto> findAllQuestion();

    public List<QuestionSimpleViewDto> findAllQuestionByUsername(String username);

    public QuestionViewDto findQuestionByBoardId(Long boardId);

    public Long saveQuestion(QuestionWriteDto saveQuestionDto);

    public void saveAnswer(AnswerWriteDto writeDto, Long boardId);

    public String changeQuestion(QuestionWriteDto questionDto, Long boardId);

    public void changeAnswer(AnswerWriteDto answerDto, Long boardId);

    public boolean checkMember(Long boardId, String boardType);

    public void removeQuestion(Long id);

    public void removeAnswer(Long id);
}
