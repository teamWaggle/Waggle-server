package com.example.waggle.domain.board.application.question;

import com.example.waggle.domain.board.presentation.dto.question.QuestionRequest;
import com.example.waggle.domain.member.persistence.entity.Member;

public interface QuestionCommandService {

    Long createQuestion(QuestionRequest createQuestionRequest,
                        Member member);

    Long updateQuestion(Long boardId,
                        QuestionRequest updateQuestionRequest,
                        Member member);

    void convertStatus(Long boardId, Member member);

    void deleteQuestionWithRelations(Long boardId, Member member);

    void deleteQuestionByAdmin(Long boardId, Member member);

}