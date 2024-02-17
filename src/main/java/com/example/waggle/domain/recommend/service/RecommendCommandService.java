package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.board.service.BoardType;

public interface RecommendCommandService {
    void handleRecommendation(Long boardId, BoardType boardType);

    void handleRecommendationByUsername(Long boardId, BoardType boardType, String username);

    void deleteRecommendByBoard(Long boardId);
}
