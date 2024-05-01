package com.example.waggle.domain.board.question.service;

import com.example.waggle.domain.board.question.entity.Question;
import com.example.waggle.domain.board.question.repository.QuestionRepository;
import com.example.waggle.domain.member.service.RedisService;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.QuestionHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.question.QuestionFilterParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class QuestionQueryServiceImpl implements QuestionQueryService {

    private final QuestionRepository questionRepository;
    private final RecommendRepository recommendRepository;
    private final RedisService redisService;

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
    public Page<Question> getPagedQuestionsByUserUrl(String userUrl, Pageable pageable) {
        return questionRepository.findByMemberUserUrl(userUrl, pageable);
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

    @Override
    public Page<Question> getPagedQuestionsByFilter(QuestionFilterParam filterParam, Pageable pageable) {
        return questionRepository.findQuestionsByFilter(filterParam, pageable);
    }

    @Override
    public List<Question> getRepresentativeQuestionList() {
        List<Question> questions = questionRepository.findAll();

        Map<Long, Long> recommendCountByQuestionId = recommendRepository.findRecommendsForQuestions().stream()
                .collect(Collectors.groupingBy(recommend -> recommend.getBoard().getId(), Collectors.counting()));

        return questions.stream()
                .sorted(Comparator.comparingLong((Question q) -> recommendCountByQuestionId.getOrDefault(q.getId(), 0L))
                        .reversed())
                .limit(3)
                .collect(Collectors.toList());

    }
}
