package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.member.entity.Member;

public interface RecommendSyncService {
    void initRecommendationInRedis(Member member);

    void syncRecommendation();
}
