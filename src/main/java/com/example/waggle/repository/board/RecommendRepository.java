package com.example.waggle.repository.board;

import com.example.waggle.domain.board.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    int countByBoardId(@Param("boardId") Long id);
    boolean existsByMemberIdAndBoardId(Long memberId, Long boardId);
    Optional<Recommend> findRecommendByMemberIdAndBoardId(Long memberId, Long boardId);

}