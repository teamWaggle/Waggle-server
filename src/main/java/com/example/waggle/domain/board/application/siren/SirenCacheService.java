package com.example.waggle.domain.board.application.siren;

import com.example.waggle.domain.board.persistence.dao.siren.jpa.SirenRepository;
import com.example.waggle.global.service.redis.RedisService;

import java.util.Objects;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class SirenCacheService {

    private final RedisService redisService;
    private final SirenRepository sirenRepository;
    private final String VIEW_COUNT_PREFIX = "viewCounts::";
    private final String BOARD_PREFIX = "board::";

    @CachePut(value = "viewCounts", key = "#boardId")
    public Long applyViewCountToRedis(Long boardId) {
        String viewCountKey = VIEW_COUNT_PREFIX + boardId;
        String currentViewCount = redisService.getValue(viewCountKey);
        if (currentViewCount != null) {
            return redisService.increment(viewCountKey);
        } else {
            Long initialViewCount = sirenRepository.findViewCountByBoardId(boardId);
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
            Long viewCount = Long.parseLong(redisService.getValue(viewCntKey));
            sirenRepository.applyViewCntToRDB(boardId, viewCount);
            redisService.deleteData(viewCntKey);
            redisService.deleteData(BOARD_PREFIX + boardId);
        }
    }

    private static Long extractBoardIdFromKey(String key) {
        return Long.parseLong(key.split("::")[1]);
    }
}
