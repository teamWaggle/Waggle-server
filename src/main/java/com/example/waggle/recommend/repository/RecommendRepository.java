package com.example.waggle.recommend.repository;

import com.example.waggle.recommend.domain.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    int countByBoardId(Long id);
    boolean existsByMemberIdAndBoardId(Long memberId, Long boardId);
    Optional<Recommend> findRecommendByMemberIdAndBoardId(Long memberId, Long boardId);

}