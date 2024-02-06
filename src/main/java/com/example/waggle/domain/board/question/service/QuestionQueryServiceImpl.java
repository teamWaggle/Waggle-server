package com.example.waggle.domain.board.question.service;

import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.repository.QuestionRepository;
import com.example.waggle.global.exception.handler.QuestionHandler;
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
public class QuestionQueryServiceImpl implements QuestionQueryService {

    private final QuestionRepository questionRepository;

    @Override
    public List<Question> getAllQuestion() {
        List<Question> questions = questionRepository.findAll();
        return questions;
    }

    @Override
    public Page<Question> getPagedQuestionsByUsername(String username, Pageable pageable) {
        return questionRepository.findByMemberUsername(username, pageable);
    }

    @Override
    public Page<Question> getPagedQuestionByMemberId(Long memberId, Pageable pageable) {
        return questionRepository.findPageByMemberId(memberId, pageable);
    }

    @Override
    public Question getQuestionByBoardId(Long boardId) {
        return questionRepository.findById(boardId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));
    }

    @Override
    public Page<Question> getPagedQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }
}
