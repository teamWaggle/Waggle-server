package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.BoardRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.member.service.RedisService;
import com.example.waggle.domain.recommend.entity.DataLocation;
import com.example.waggle.domain.recommend.entity.Recommend;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.exception.handler.RecommendHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RecommendCommandServiceImplV2 implements RecommendCommandService {
    private final RecommendRepository recommendRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final RedisService redisService;
    private static String RECOMMEND_SET_KEY_PATTERN = "set:recommend:board:";
    private static String RECOMMEND_HASH_KEY_PATTERN = "hash:recommend:board:";

    @Override
    public void handleRecommendation(Long boardId, Member member) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOARD_NOT_FOUND));
        validateRecommendMyself(member, board);

        String set_key = RECOMMEND_SET_KEY_PATTERN + boardId;
        String hash_key = RECOMMEND_HASH_KEY_PATTERN + boardId;
        String hash_hashKey = String.valueOf(member.getId());
        log.info("hashkey = {}", hash_hashKey);
        //if total count don't exist in redis, initialize.
        if (redisService.existCntInHash(hash_key, hash_hashKey)) {
            log.info("recommend = {}", recommendRepository.countByBoardId(boardId));
            redisService.setCntInHash(hash_key, hash_hashKey, Long.valueOf(recommendRepository.countByBoardId(boardId)));
        }

        boolean existInRedis = redisService.existValueInSet(set_key, hash_hashKey);
        boolean existInRDB = recommendRepository.existsByMemberAndBoard(member, board);

        DataLocation dataLocation = checkLocation(existInRedis, existInRDB);

        switch (dataLocation) {
            case IN_REDIS -> {
                redisService.deleteValueInSet(set_key, hash_hashKey);
                redisService.incrementCntInHash(hash_key, hash_hashKey, -1L);
            }
            case IN_RDB -> {
                cancelRecommendation(board, member);
                redisService.incrementCntInHash(hash_key, hash_hashKey, -1L);
            }
            case NO_WHERE -> {
                redisService.setValueInSet(set_key, hash_hashKey);
                redisService.incrementCntInHash(hash_key, hash_hashKey, 1L);
            }
            default -> throw new RecommendHandler(ErrorStatus.RECOMMEND_NOT_FOUND);
        }
    }

    private DataLocation checkLocation(boolean existInRedis, boolean existInRDB) {
        DataLocation dataLocation = DataLocation.NO_WHERE;
        if (existInRedis) {
            dataLocation = DataLocation.IN_REDIS;
        } else {
            if (existInRDB) {
                dataLocation = DataLocation.IN_RDB;
            }
        }
        return dataLocation;
    }

    private static void validateRecommendMyself(Member member, Board board) {
        if (board.getMember().equals(member)) {
            throw new RecommendHandler(ErrorStatus.BOARD_CANNOT_RECOMMEND_OWN);
        }
    }

    private void createRecommendation(Board board, Member member) {
        Recommend recommend = Recommend.builder().board(board).member(member).build();
        recommendRepository.save(recommend);
    }

    private void cancelRecommendation(Board board, Member member) {
        Recommend recommend = recommendRepository
                .findRecommendByMemberAndBoard(member, board)
                .orElseThrow(() -> new RecommendHandler(ErrorStatus.RECOMMEND_NOT_FOUND));
        recommendRepository.delete(recommend);
    }

    //@Scheduled(fixedRate = 1000 * 60 * 60L)
    public void syncRecommend() {
        Set<String> keysByPattern = redisService.getKeysByPattern(RECOMMEND_SET_KEY_PATTERN);
        keysByPattern.forEach(key -> {
            Long boardId = Long.valueOf(key.substring(0, RECOMMEND_SET_KEY_PATTERN.length() - 1));
            Set<String> valuesInSet = redisService.getValuesInSet(key);
            valuesInSet.forEach(value -> {
                Board board = boardRepository.findById(boardId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.BOARD_NOT_FOUND));
                Member member = memberRepository.findById(Long.valueOf(value))
                        .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
                createRecommendation(board, member);
            });
        });
    }

    @Override
    public void deleteRecommendByBoard(Long boardId) {

    }
}
