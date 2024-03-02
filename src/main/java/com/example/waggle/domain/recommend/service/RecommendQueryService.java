package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.web.dto.recommend.RecommendResponse.RecommendationInfo;

import java.util.List;

public interface RecommendQueryService {

    boolean checkRecommend(Long boardId, String username);

    int countRecommend(Long boardId);

    List<Member> getRecommendingMembers(Long boardId);

    RecommendationInfo getRecommendationInfo(Long boardId, String username);

}
