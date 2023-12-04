package com.example.waggle.domain.board.question.service;

import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.repository.QuestionRepository;
import com.example.waggle.global.exception.handler.QuestionHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.answer.AnswerDetailDto;
import com.example.waggle.web.dto.question.QuestionDetailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class QuestionQueryServiceImpl implements QuestionQueryService {

    private final QuestionRepository questionRepository;

//    @Override
//    public List<Question> getQuestions() {
//        List<Question> questions = questionRepository.findAll();
//        return questions;
//    }

    @Override
    public Page<Question> getPagedQuestionsByUsername(String username, Pageable pageable) {
        return questionRepository.findByMemberUsername(username, pageable);
    }

    @Override
    public QuestionDetailDto getQuestionByBoardId(Long boardId) {
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));

        List<AnswerDetailDto> answerDetailDtos = question.getAnswers().stream()
                .map(AnswerDetailDto::toDto).collect(Collectors.toList());

        QuestionDetailDto questionDetailDto = QuestionDetailDto.toDto(question);
        questionDetailDto.linkAnswerView(answerDetailDtos);

        return questionDetailDto;
    }

    @Override
    public Page<Question> getPagedQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }
}
