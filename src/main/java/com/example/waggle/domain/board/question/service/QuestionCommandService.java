package com.example.waggle.domain.board.question.service;

import com.example.waggle.web.dto.question.QuestionRequest;

public interface QuestionCommandService {

    Long createQuestion(QuestionRequest.QuestionWriteDto request);



    Long updateQuestion(Long boardId, QuestionRequest.QuestionWriteDto request);


    void deleteQuestion(Long boardId);

}
