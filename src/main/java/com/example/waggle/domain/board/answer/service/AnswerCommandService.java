package com.example.waggle.domain.board.answer.service;

import com.example.waggle.web.dto.answer.AnswerRequest;

import java.io.IOException;

public interface AnswerCommandService {
    Long createAnswer(Long questionId, AnswerRequest.Post answerWriteDto) throws IOException;

    Long updateAnswer(Long boardId, AnswerRequest.Post answerWriteDto) throws IOException;

    void deleteAnswer(Long boardId);

}
