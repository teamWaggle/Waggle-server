package com.example.waggle.repository.board;

import com.example.waggle.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query(value = "SELECT count(*) FROM like as l where l.board_id = :boardId", nativeQuery = true)
    int findCount(@Param("boardId")Long boardId);
}