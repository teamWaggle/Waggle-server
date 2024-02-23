package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.member.entity.Member;

public interface RecommendCommandService {
    void handleRecommendation(Long boardId);

    void handleRecommendation(Long boardId, Member member);

    void deleteRecommendByBoard(Long boardId);
}
