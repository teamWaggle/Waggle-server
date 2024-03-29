package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.member.service.RedisService;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.exception.handler.RecommendHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.recommend.RecommendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        if (redisService.existIsRecommend(boardId, member.getId())) {
            return true;
        }
        return false;
    }

    private void validateInitRecommend(Member member) {
        if (redisService.existInitRecommend(member.getId())) {
            throw new RecommendHandler(ErrorStatus.RECOMMEND_WAS_NOT_INITIATED);
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
        return null;
    }

    @Override
    public RecommendResponse.RecommendationInfo getRecommendationInfo(Long boardId, String username) {
        return null;
    }
}
