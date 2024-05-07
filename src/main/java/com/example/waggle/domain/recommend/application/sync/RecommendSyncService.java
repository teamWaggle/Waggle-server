package com.example.waggle.domain.recommend.application.sync;

import com.example.waggle.domain.member.persistence.entity.Member;

public interface RecommendSyncService {
    void initRecommendationInRedis(Member member);

    void syncRecommendation();

}
