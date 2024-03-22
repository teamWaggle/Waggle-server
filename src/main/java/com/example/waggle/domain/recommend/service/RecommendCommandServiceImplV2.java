package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.board.BoardRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.RedisService;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RecommendCommandServiceImplV2 implements RecommendCommandService {
    private final RecommendRepository recommendRepository;
    private final BoardRepository boardRepository;
    private final RedisService redisService;

    @Override
    public void handleRecommendation(Long boardId, Member member) {

    }

    @Override
    public void deleteRecommendByBoard(Long boardId) {

    }
}
