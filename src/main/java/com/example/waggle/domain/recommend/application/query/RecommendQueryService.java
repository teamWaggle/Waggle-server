package com.example.waggle.domain.recommend.application.query;

import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.recommend.presentation.dto.RecommendResponse.RecommendationInfo;

import java.util.List;

public interface RecommendQueryService {

    boolean checkRecommend(Long boardId, String username);

    int countRecommend(Long boardId);

    List<Member> getRecommendingMembers(Long boardId);

    RecommendationInfo getRecommendationInfo(Long boardId, String username);

}
