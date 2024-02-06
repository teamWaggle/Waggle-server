package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.recommend.entity.Recommend;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RecommendQueryServiceImpl implements RecommendQueryService {

    private final RecommendRepository recommendRepository;
    private final MemberQueryService memberQueryService;

    @Override
    public boolean checkRecommend(Long boardId, Long memberId) {
        if (!memberQueryService.isAuthenticated()) {
            return false;
        }
        Member signInMember = memberQueryService.getMemberByUsername(SecurityUtil.getCurrentUsername());

        boolean recommendIt = false;
        //(login user == board writer) checking
        if (signInMember.getId() != memberId) {
            recommendIt = recommendRepository
                    .existsByMemberIdAndBoardId(signInMember.getId(), boardId);
        }
        return recommendIt;
    }

    @Override
    public int countRecommend(Long boardId) {
        return recommendRepository.countByBoardId(boardId);
    }

    @Override
    public List<Member> getRecommendingMembers(Long boardId) {
        List<Recommend> byBoardId = recommendRepository.findByBoardId(boardId);
        return byBoardId.stream().map(r -> r.getMember()).collect(Collectors.toList());
    }
}
