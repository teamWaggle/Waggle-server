package com.example.waggle.domain.board.question.service;

import com.example.waggle.web.dto.question.QuestionRequest;

import java.util.List;

public interface QuestionCommandService {

    Long createQuestion(QuestionRequest.QuestionWriteDto request, List<String> uploadedFiles);



    Long updateQuestion(Long boardId, QuestionRequest.QuestionWriteDto request, List<String> uploadedFiles);


    void deleteQuestion(Long boardId);

}
