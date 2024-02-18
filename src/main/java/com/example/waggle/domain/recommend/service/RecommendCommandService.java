package com.example.waggle.domain.recommend.service;

public interface RecommendCommandService {
    void handleRecommendation(Long boardId);

    void handleRecommendationByUsername(Long boardId, String username);

    void deleteRecommendByBoard(Long boardId);
}
