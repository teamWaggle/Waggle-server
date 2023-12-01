package com.example.waggle.domain.board.question.service;

import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.repository.QuestionRepository;
import com.example.waggle.global.exception.CustomPageException;
import com.example.waggle.web.dto.answer.AnswerDetailDto;
import com.example.waggle.web.dto.question.QuestionDetailDto;
import com.example.waggle.web.dto.question.QuestionSummaryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.waggle.global.exception.ErrorCode.BOARD_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class QuestionQueryServiceImpl implements QuestionQueryService{

    private final QuestionRepository questionRepository;

    @Override
    public List<QuestionSummaryDto> getQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questions.stream().map(QuestionSummaryDto::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<QuestionSummaryDto> getPagedQuestionsByUsername(String username, Pageable pageable) {
        Page<Question> questionByUsername = questionRepository.findByMemberUsername(username, pageable);
        return questionByUsername.map(QuestionSummaryDto::toDto);
    }

    @Override
    public QuestionDetailDto getQuestionByBoardId(Long boardId) {
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));


        List<AnswerDetailDto> answerDetailDtos = question.getAnswers().stream()
                .map(AnswerDetailDto::toDto).collect(Collectors.toList());

        QuestionDetailDto questionDetailDto = QuestionDetailDto.toDto(question);
        questionDetailDto.linkAnswerView(answerDetailDtos);

        return questionDetailDto;
    }

    @Override
    public Page<QuestionSummaryDto> getPagedQuestions(Pageable pageable) {
        Page<Question> all = questionRepository.findAll(pageable);
        return all.map(QuestionSummaryDto::toDto);
    }
}
