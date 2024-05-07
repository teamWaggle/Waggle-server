package com.example.waggle.domain.recommend.application.command;

import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.recommend.application.sync.RecommendSyncService;
import com.example.waggle.global.service.redis.RedisService;
import com.example.waggle.domain.recommend.persistence.entity.Recommend;
import com.example.waggle.domain.recommend.persistence.dao.RecommendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RecommendCommandServiceImplV2 implements RecommendCommandService {
    private final RecommendRepository recommendRepository;
    private final RedisService redisService;
    private final RecommendSyncService recommendSyncService;


    @Override
    public void handleRecommendation(Long boardId, Member member) {
        validateInitRecommend(member);
        validateInitRecommendCnt(boardId);
        if (redisService.existRecommend(member.getId(), boardId)) {
            redisService.deleteRecommend(member.getId(), boardId);
            redisService.decrementRecommendCnt(boardId);
        } else {
            redisService.setRecommend(member.getId(), boardId);
            redisService.incrementRecommendCnt(boardId);
        }
    }

    private void validateInitRecommendCnt(Long boardId) {
        if (!redisService.existRecommendCnt(boardId)) {
            redisService.setRecommendCnt(boardId, Long.valueOf(recommendRepository.countByBoardId(boardId)));
        }
    }

    private void validateInitRecommend(Member member) {
        if (!redisService.existInitRecommend(member.getId())) {
            recommendSyncService.initRecommendationInRedis(member);
        }
    }


    @Override
    public void deleteRecommendByBoard(Long boardId) {
        List<Recommend> byBoardId = recommendRepository.findByBoardId(boardId);
        byBoardId.stream().forEach(r -> recommendRepository.delete(r));
    }
}
