package com.example.waggle.domain.recommend.application.sync;

import com.example.waggle.domain.board.persistence.dao.board.BoardRepository;
import com.example.waggle.domain.board.persistence.entity.Board;
import com.example.waggle.domain.member.persistence.dao.MemberRepository;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.recommend.persistence.dao.RecommendRepository;
import com.example.waggle.domain.recommend.persistence.entity.Recommend;
import com.example.waggle.exception.object.handler.RecommendHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.global.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        // isRecommend 데이터 rdb -> redis
        List<Long> boardIdList = recommendRepository.findByMember(member).stream()
                .map(recommend -> recommend.getBoard().getId()).collect(Collectors.toList());
        boardIdList.forEach(boardId -> redisService.setRecommend(member.getId(), boardId));
    }

    private void validateInitRecommend(Member member) {
        if (redisService.existInitRecommend(member.getId())) {
            throw new RecommendHandler(ErrorStatus.RECOMMEND_WAS_ALREADY_INITIATED);
        }
    }

    @Scheduled(fixedRate = 1000 * 60 * 60 * 3L)
//    @Scheduled(fixedRate = 1000 * 60 * 3L)
    @Override
    public void syncRecommendation() {
        redisService.getAllRecommendingMemberList().stream()
                .map(memberRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(member -> {
                    recommendRepository.deleteAllByMember(member);
                    redisService.getRecommendedBoardList(member.getId()).stream()
                            .map(boardRepository::findById)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .map(board -> buildRecommend(member, board))
                            .forEach(recommend -> recommendRepository.save(recommend));
                });
        redisService.clearRecommend();
        redisService.clearRecommendCnt();
        redisService.clearInitRecommend();
    }


    private static Recommend buildRecommend(Member member, Board board) {
        return Recommend.builder()
                .member(member)
                .board(board)
                .build();
    }
}
