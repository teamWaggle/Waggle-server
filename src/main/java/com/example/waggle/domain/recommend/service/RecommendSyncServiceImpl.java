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
public class RecommendSyncServiceImpl implements RecommendSyncService {
    private final RedisService redisService;
    private final RecommendRepository recommendRepository;

    @Override
    public void initRecommendationInRedis(Member member) {
        if (redisService.existInitRecommend(member.getId())) {
            throw new RecommendHandler(ErrorStatus.RECOMMEND_WAS_ALREADY_INITIATED);
        }
        //초기화 선언
        redisService.initRecommend(member.getId());
        // cnt, isRecommend관련 데이터 rdb -> redis
        List<Recommend> dataList = recommendRepository.findByMember(member);
        dataList.forEach(data -> redisService.setIsRecommend(data.getId(), member.getId()));
    }

    @Override
    public void syncRecommendation() {

    }
}
