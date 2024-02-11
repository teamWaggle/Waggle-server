package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.member.entity.Member;

import java.util.List;

public interface RecommendQueryService {

    boolean checkRecommend(Long boardId, Long memberId);

    int countRecommend(Long boardId);

    List<Member> getRecommendingMembers(Long boardId);
}
