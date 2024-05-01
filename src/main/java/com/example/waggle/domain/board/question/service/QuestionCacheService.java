package com.example.waggle.domain.board.question.service;

import com.example.waggle.domain.board.question.repository.QuestionRepository;
import com.example.waggle.domain.member.service.RedisService;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class QuestionCacheService {

    private final RedisService redisService;
    private final QuestionRepository questionRepository;
    private final String VIEW_COUNT_PREFIX = "viewCount::";
    private final String BOARD_PREFIX = "board::";

    @CachePut(value = "viewCounts", key = "#boardId")
    public Long applyViewCountToRedis(Long boardId) {
        String viewCountKey = VIEW_COUNT_PREFIX + boardId;
        String currentViewCount = redisService.getValue(viewCountKey);
        if (currentViewCount != null) {
            return redisService.increment(viewCountKey);
        } else {
            Long initialViewCount = questionRepository.findViewCountByBoardId(boardId);
            return initialViewCount + 1;
        }
    }

    @Scheduled(fixedRate = 1000 * 60 * 3)
    public void applyViewCountToRDB() {
        Set<String> viewCountKeys = redisService.getKeysByPattern(VIEW_COUNT_PREFIX + "*");
        if (Objects.requireNonNull(viewCountKeys).isEmpty()) {
            return;
        }

        for (String viewCntKey : viewCountKeys) {
            Long boardId = extractBoardIdFromKey(viewCntKey);
            Long viewCount = Long.parseLong(redisService.getData(viewCntKey));
            questionRepository.applyViewCntToRDB(boardId, viewCount);
            redisService.deleteData(viewCntKey);
            redisService.deleteData(BOARD_PREFIX + boardId);
        }
    }

    private static Long extractBoardIdFromKey(String key) {
        return Long.parseLong(key.split("::")[1]);
    }
}
