package com.example.waggle.domain.recommend.application.query;

import com.example.waggle.domain.member.persistence.dao.MemberRepository;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.recommend.persistence.dao.RecommendRepository;
import com.example.waggle.domain.recommend.presentation.dto.RecommendResponse;
import com.example.waggle.exception.object.handler.MemberHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.global.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RecommendQueryServiceImplV2 implements RecommendQueryService {
    private final MemberRepository memberRepository;
    private final RecommendRepository recommendRepository;
    private final RedisService redisService;

    @Override
    public boolean checkRecommend(Long boardId, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.RECOMMEND_NOT_FOUND));
        validateInitRecommend(member);
        if (redisService.existRecommend(member.getId(), boardId)) {
            return true;
        }
        return false;
    }

    private void validateInitRecommend(Member member) {
        if (!redisService.existInitRecommend(member.getId())) {
            redisService.initRecommend(member.getId());
            // isRecommend 데이터 rdb -> redis
            recommendRepository.findByMember(member).stream()
                    .map(recommend -> recommend.getBoard().getId())
                    .forEach(boardId -> redisService.setRecommend(member.getId(), boardId));
        }
    }

    @Override
    public int countRecommend(Long boardId) {
        if (redisService.existRecommendCnt(boardId)) {
            return Math.toIntExact(redisService.getRecommendCnt(boardId));
        }
        redisService.setRecommendCnt(boardId, Long.valueOf(recommendRepository.countByBoardId(boardId)));
        return recommendRepository.countByBoardId(boardId);
    }

    @Override
    public List<Member> getRecommendingMembers(Long boardId) {
        List<Member> fromRDB = recommendRepository.findByBoardId(boardId).stream()
                .map(recommend -> recommend.getMember()).collect(Collectors.toList());
        List<Member> fromRedis = redisService.getAllRecommendingMemberList().stream()
                .filter(memberId -> redisService.existRecommend(memberId, boardId))
                .map(memberRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toList());
        fromRDB.addAll(fromRedis);
        return fromRDB;
    }

    @Override
    public RecommendResponse.RecommendationInfo getRecommendationInfo(Long boardId, String username) {
        return null;
    }
}