package com.example.waggle.domain.recommend.repository;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.recommend.entity.Recommend;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    int countByBoardId(Long id);

    boolean existsByMemberAndBoard(Member member, Board board);

    Optional<Recommend> findRecommendByMemberAndBoard(Member member, Board board);

    List<Recommend> findByBoardId(Long boardId);

    void deleteAllByBoardId(Long boardId);

    void deleteAllByMemberUsername(String username);

    void deleteAllByMember(Member member);

    void deleteAllByBoard(Board board);

    @Query("SELECT r FROM Recommend r WHERE r.board.id IN (SELECT s.id FROM Siren s)")
    List<Recommend> findRecommendsForSirens();

}