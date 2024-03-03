package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.BoardRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.recommend.entity.Recommend;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.SecurityUtil;
import java.util.List;
import java.util.stream.Collectors;
import com.example.waggle.web.dto.recommend.RecommendResponse.RecommendationInfo;
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
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Override
    public boolean checkRecommend(Long boardId, String username) {
        if ("anonymousUser".equals(username)) {
            return false;
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOARD_NOT_FOUND));

        Member signInMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return recommendRepository.existsByMemberAndBoard(signInMember, board);
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

    @Override
    public RecommendationInfo getRecommendationInfo(Long boardId, String username) {
        return RecommendationInfo.builder()
                .isRecommend(checkRecommend(boardId, username))
                .recommendCount(countRecommend(boardId))
                .build();
    }


}
