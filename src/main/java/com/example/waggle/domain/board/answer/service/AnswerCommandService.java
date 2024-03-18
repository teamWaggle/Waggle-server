package com.example.waggle.domain.board.answer.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.answer.AnswerRequest;


public interface AnswerCommandService {
    Long createAnswer(Long questionId,
                      AnswerRequest createAnswerRequest,
                      Member member);

    Long updateAnswer(Long boardId,
                      AnswerRequest updateAnswerRequest,
                      Member member);

    void deleteAnswer(Long boardId, Member member);
}
