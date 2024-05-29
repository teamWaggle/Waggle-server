package com.example.waggle.domain.board.application.question;

import com.example.waggle.domain.board.persistence.dao.question.jpa.QuestionRepository;
import com.example.waggle.domain.board.persistence.entity.Question;
import com.example.waggle.domain.board.presentation.dto.question.QuestionSortParam;
import com.example.waggle.domain.recommend.persistence.dao.RecommendRepository;
import com.example.waggle.exception.object.handler.QuestionHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.global.service.redis.RedisService;
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
    public Page<Question> getPagedQuestionListByUsername(String username, Pageable pageable) {
        return questionRepository.findByMemberUsername(username, pageable);
    }

    @Override
    public Page<Question> getPagedQuestionListByUserUrl(String userUrl, Pageable pageable) {
        return questionRepository.findByMemberUserUrl(userUrl, pageable);
    }

    @Override
    public Page<Question> getPagedQuestionListByMemberId(Long memberId, Pageable pageable) {
        return questionRepository.findPageByMemberId(memberId, pageable);
    }

    @Override
    public Question getQuestionByBoardId(Long boardId) {
        return questionRepository.findById(boardId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));
    }

    @Override
    public Page<Question> getPagedQuestionList(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    @Override
    public Page<Question> getPagedQuestionListBySortParam(QuestionSortParam sortParam, Pageable pageable) {
        return questionRepository.findQuestionsBySortParam(sortParam, pageable);
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

    @Override
    public Page<Question> getPagedQuestionListByKeyword(String keyword, Pageable pageable) {
        return questionRepository.findQuestionsByKeyword(keyword, pageable);
    }

    @Override
    public Page<Question> getPagedQuestionListByKeywordAndSortParam(String keyword, QuestionSortParam sortParam, Pageable pageable) {
        return questionRepository.findQuestionListByKeywordAndSortParam(keyword, sortParam, pageable);
    }
}
