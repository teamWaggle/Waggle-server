package com.example.waggle.domain.recommend.repository;

import com.example.waggle.domain.recommend.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    int countByBoardId(Long id);
    boolean existsByMemberIdAndBoardId(Long memberId, Long boardId);
    Optional<Recommend> findRecommendByMemberIdAndBoardId(Long memberId, Long boardId);
    List<Recommend> findByBoardId(Long boardId);

}