package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.RedisService;
import com.example.waggle.domain.recommend.entity.Recommend;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.RecommendHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
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


    @Override
    public void handleRecommendation(Long boardId, Member member) {
        validateInitRecommend(member);
        if (redisService.existIsRecommend(boardId, member.getId())) {
            redisService.deleteIsRecommend(boardId, member.getId());
            redisService.decrementRecommendCnt(boardId);
        } else {
            redisService.setIsRecommend(boardId, member.getId());
            redisService.incrementRecommendCnt(boardId);
        }
    }

    private void validateInitRecommend(Member member) {
        if (redisService.existInitRecommend(member.getId())) {
            throw new RecommendHandler(ErrorStatus.RECOMMEND_WAS_NOT_INITIATED);
        }
    }


    @Override
    public void deleteRecommendByBoard(Long boardId) {
        List<Recommend> byBoardId = recommendRepository.findByBoardId(boardId);
        byBoardId.stream().forEach(r -> recommendRepository.delete(r));
    }
}
