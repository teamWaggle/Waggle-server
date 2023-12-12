package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.board.entity.Board;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.recommend.entity.Recommend;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.RecommendHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.domain.board.service.BoardType;
import com.example.waggle.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RecommendCommandServiceImpl implements RecommendCommandService {
    private final BoardService boardService;
    private final MemberQueryService memberQueryService;
    private final RecommendRepository recommendRepository;

    @Override
    public void handleRecommendation(Long boardId, BoardType boardType) {
        Board board = boardService.getBoard(boardId, boardType);
        Member member = memberQueryService.getSignInMember();
        //boardWriter.equals(user)
        if (board.getMember().equals(member)) {
            log.info("can't recommend mine");
            throw new RecommendHandler(ErrorStatus.BOARD_CANNOT_RECOMMEND_OWN);
        }

        boolean isRecommended = recommendRepository.existsByMemberIdAndBoardId(member.getId(), board.getId());
        if (isRecommended) {
            log.info("cancel recommend");
            cancelRecommendation(member.getId(), boardId);
        }
        else{
            log.info("click recommend");
            createRecommendation(board, member);
        }
    }

    private void cancelRecommendation(Long memberId, Long boardId) {
        Recommend recommend = recommendRepository
                .findRecommendByMemberIdAndBoardId(memberId, boardId)
                .orElseThrow(() -> new RecommendHandler(ErrorStatus.RECOMMEND_NOT_FOUND));
        recommendRepository.delete(recommend);
    }

    private void createRecommendation(Board board, Member member) {
        Recommend recommend = Recommend.builder().board(board).member(member).build();
        recommendRepository.save(recommend);
    }

    @Override
    public void deleteRecommendByBoard(Long boardId) {
        List<Recommend> byBoardId = recommendRepository.findByBoardId(boardId);
        byBoardId.stream().forEach(r -> recommendRepository.delete(r));
    }
}
