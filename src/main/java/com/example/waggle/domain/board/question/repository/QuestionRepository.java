package com.example.waggle.domain.board.question.repository;

import com.example.waggle.domain.board.question.entity.Question;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findListByMemberUsername(String username);

    Page<Question> findAll(Pageable pageable);

    Page<Question> findByMemberUsername(String username, Pageable pageable);

    Page<Question> findPageByMemberId(Long memberId, Pageable pageable);

    @Query("SELECT q.viewCount FROM Question q WHERE q.id = :boardId")
    Long findViewCountByBoardId(@Param("boardId") Long boardId);

    @Query("update Question q set q.viewCount = :viewCount where q.id = :boardId")
    void applyViewCntToRDB(@Param("boardId") Long boardId, @Param("viewCount") Long viewCount);

}