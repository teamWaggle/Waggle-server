package com.example.waggle.domain.recommend.application.command;

import com.example.waggle.domain.member.persistence.entity.Member;

public interface RecommendCommandService {

    void handleRecommendation(Long boardId, Member member);

    void deleteRecommendByBoard(Long boardId);

}
