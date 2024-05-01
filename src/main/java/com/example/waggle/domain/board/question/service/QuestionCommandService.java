package com.example.waggle.domain.board.question.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.question.QuestionRequest;

public interface QuestionCommandService {

    Long createQuestion(QuestionRequest createQuestionRequest,
                        Member member);

    Long updateQuestion(Long boardId,
                        QuestionRequest updateQuestionRequest,
                        Member member);

    void convertStatus(Long boardId, Member member);

    void deleteQuestion(Long boardId, Member member);

}