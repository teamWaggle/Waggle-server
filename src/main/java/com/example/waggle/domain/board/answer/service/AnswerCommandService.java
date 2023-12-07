package com.example.waggle.domain.board.answer.service;

import com.example.waggle.web.dto.answer.AnswerRequest;

import java.io.IOException;
import java.util.List;

public interface AnswerCommandService {
    Long createAnswer(Long questionId,
                      AnswerRequest.Post answerWriteDto,
                      List<String> uploadFiles) throws IOException;

    Long updateAnswer(Long boardId,
                      AnswerRequest.Post answerWriteDto,
                      List<String> uplodFiles) throws IOException;

    void deleteAnswer(Long boardId);

}
