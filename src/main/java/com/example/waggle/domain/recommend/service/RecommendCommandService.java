package com.example.waggle.domain.recommend.service;

import com.example.waggle.global.util.service.BoardType;

public interface RecommendCommandService {
    void handleRecommendation(Long boardId, BoardType boardType);

    void deleteRecommendByBoard(Long boardId);
}
