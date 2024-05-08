package com.example.waggle.domain.recommend.persistence.dao;

import com.example.waggle.domain.board.persistence.entity.Board;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.recommend.persistence.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {

    int countByBoardId(Long id);

    boolean existsByMemberAndBoard(Member member, Board board);

    Optional<Recommend> findRecommendByMemberAndBoard(Member member, Board board);

    List<Recommend> findByBoardId(Long boardId);

    List<Recommend> findByMember(Member member);

    void deleteAllByBoardId(Long boardId);

    void deleteAllByMemberUsername(String username);

    void deleteAllByMember(Member member);

    void deleteAllByBoard(Board board);

    void deleteByBoardAndMember(Board board, Member member);

    @Query("SELECT r FROM Recommend r WHERE r.board.id IN (SELECT s.id FROM Siren s)")
    List<Recommend> findRecommendsForSirens();

    @Query("SELECT r FROM Recommend r WHERE r.board.id IN (SELECT q.id FROM Question q)")
    List<Recommend> findRecommendsForQuestions();

}