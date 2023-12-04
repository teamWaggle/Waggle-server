package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.recommend.entity.Recommend;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.util.service.BoardType;
import com.example.waggle.global.util.service.UtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.waggle.global.exception.ErrorCode.CANNOT_RECOMMEND_MYSELF;
import static com.example.waggle.global.exception.ErrorCode.RECOMMEND_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RecommendCommandServiceImpl implements RecommendCommandService {
    private final UtilService utilService;
    private final RecommendRepository recommendRepository;

    @Override
    public void handleRecommendation(Long boardId, BoardType boardType) {
        Board board = utilService.getBoard(boardId, boardType);
        Member member = utilService.getSignInMember();
        boolean isRecommended = recommendRepository.existsByMemberIdAndBoardId(member.getId(), board.getId());
        if (isRecommended) {
            cancelRecommendation(member.getId(), boardId);
        }
        else{
            if (board.getMember().equals(member)) {
                throw new CustomPageException(CANNOT_RECOMMEND_MYSELF);
            }
            createRecommendation(board, member);
        }
    }

    private void cancelRecommendation(Long memberId, Long boardId) {
        Recommend recommend = recommendRepository
                .findRecommendByMemberIdAndBoardId(memberId, boardId)
                .orElseThrow(() -> new CustomPageException(RECOMMEND_NOT_FOUND));
        recommendRepository.delete(recommend);
    }

    private void createRecommendation(Board board, Member member) {
        Recommend recommend = Recommend.builder().board(board).member(member).build();
        recommendRepository.save(recommend);
    }
}
