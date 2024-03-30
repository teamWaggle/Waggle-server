package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.RedisService;
import com.example.waggle.domain.recommend.entity.Recommend;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.RecommendHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Transactional
//@Service
public class RecommendCommandServiceImplV2 implements RecommendCommandService {
    private final RecommendRepository recommendRepository;
    private final RedisService redisService;


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
            throw new RecommendHandler(ErrorStatus.RECOMMEND_WAS_NOT_INITIATED);
        }
    }


    @Override
    public void deleteRecommendByBoard(Long boardId) {
        List<Recommend> byBoardId = recommendRepository.findByBoardId(boardId);
        byBoardId.stream().forEach(r -> recommendRepository.delete(r));
    }
}
