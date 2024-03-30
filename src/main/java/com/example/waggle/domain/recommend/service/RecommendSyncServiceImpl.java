package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.BoardRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.member.service.RedisService;
import com.example.waggle.domain.recommend.entity.Recommend;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.RecommendHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RecommendSyncServiceImpl implements RecommendSyncService {
    private final RecommendRepository recommendRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final RedisService redisService;

    @Override
    public void initRecommendationInRedis(Member member) {
        validateInitRecommend(member);
        //초기화 선언
        redisService.initRecommend(member.getId());
        log.info("init = {}", redisService.existInitRecommend(member.getId()));
        // isRecommend 데이터 rdb -> redis
        List<Recommend> dataList = recommendRepository.findByMember(member);
        dataList.forEach(data -> redisService.setRecommend(data.getId(), member.getId()));
    }

    private void validateInitRecommend(Member member) {
        if (redisService.existInitRecommend(member.getId())) {
            throw new RecommendHandler(ErrorStatus.RECOMMEND_WAS_ALREADY_INITIATED);
        }
    }

    @Override
    public void syncRecommendation() {
        redisService.getAllRecommendingMemberList().stream()
                .map(memberRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(member -> {
                    redisService.getRecommendedBoardList(member.getId()).stream()
                            .map(boardRepository::findById)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .map(board -> buildRecommend(member, board))
                            .forEach(recommend -> recommendRepository.save(recommend));
                });
        redisService.clearRecommend();
    }


    private static Recommend buildRecommend(Member member, Board board) {
        return Recommend.builder()
                .member(member)
                .board(board)
                .build();
    }
}
