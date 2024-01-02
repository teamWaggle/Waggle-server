package com.example.waggle.domain.board.answer.service;

import com.example.waggle.domain.board.answer.entity.Answer;
import com.example.waggle.domain.board.answer.repository.AnswerRepository;
import com.example.waggle.global.exception.handler.AnswerHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AnswerQueryServiceImpl implements AnswerQueryService{

    private final AnswerRepository answerRepository;
    @Override
    public Page<Answer> getPagedAnswerByUsername(String username, Pageable pageable) {
        return answerRepository.findPagedAnswerByMemberUsername(username, pageable);
    }

    @Override
    public Answer getAnswerByBoardId(Long boardId) {
        return answerRepository.findById(boardId)
                .orElseThrow(()->new AnswerHandler(ErrorStatus.BOARD_NOT_FOUND));
    }

    @Override
    public Page<Answer> getPagedAnswers(Long questionId, Pageable pageable) {
        return answerRepository.findByQuestionId(questionId,pageable);
    }

    @Override
    public List<Answer> getAnswersByQuestion(Long questionId) {
        return answerRepository.findAnswerByQuestionId(questionId);
    }


}
