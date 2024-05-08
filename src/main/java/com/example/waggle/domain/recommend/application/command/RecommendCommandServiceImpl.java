package com.example.waggle.domain.recommend.application.command;

import com.example.waggle.domain.board.persistence.dao.board.BoardRepository;
import com.example.waggle.domain.board.persistence.entity.Board;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.recommend.persistence.dao.RecommendRepository;
import com.example.waggle.domain.recommend.persistence.entity.Recommend;
import com.example.waggle.exception.object.general.GeneralException;
import com.example.waggle.exception.object.handler.RecommendHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
//@Service
public class RecommendCommandServiceImpl implements RecommendCommandService {
    private final RecommendRepository recommendRepository;
    private final BoardRepository boardRepository;

    @Override
    public void handleRecommendation(Long boardId, Member member) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOARD_NOT_FOUND));
//        validateRecommendMyself(member, board);

        boolean isRecommended = recommendRepository.existsByMemberAndBoard(member, board);
        if (isRecommended) {
            cancelRecommendation(board, member);
        } else {
            createRecommendation(board, member);
        }
    }

    private static void validateRecommendMyself(Member member, Board board) {
        if (board.getMember().equals(member)) {
            throw new RecommendHandler(ErrorStatus.BOARD_CANNOT_RECOMMEND_OWN);
        }
    }

    private void cancelRecommendation(Board board, Member member) {
        Recommend recommend = recommendRepository
                .findRecommendByMemberAndBoard(member, board)
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
