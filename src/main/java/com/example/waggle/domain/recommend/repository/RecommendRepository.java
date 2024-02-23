package com.example.waggle.domain.recommend.repository;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.recommend.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    int countByBoardId(Long id);

    boolean existsByMemberIdAndBoardId(Long memberId, Long boardId);

    Optional<Recommend> findRecommendByMemberAndBoard(Member member, Board board);

    List<Recommend> findByBoardId(Long boardId);

    void deleteAllByBoardId(Long boardId);

    void deleteAllByMemberUsername(String username);

    void deleteAllByMember(Member member);

    void deleteAllByBoard(Board board);

}