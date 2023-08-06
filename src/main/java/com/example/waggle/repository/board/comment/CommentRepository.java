package com.example.waggle.repository.board.comment;

import com.example.waggle.domain.board.comment.Comment;
import com.example.waggle.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    //만약 boardId를 가지는 comment가 없다면 0을, 이외에는 max 값을 가져오도록 한다.
//    @Query(value = "SELECT IFNULL(MAX(c.orders), 0) FROM comment AS c" +
//            " WHERE c.board_id = :boardId",nativeQuery = true)
//    int findLastOrderByBoardId(@Param("boardId") Long Id);
    List<Comment> findByBoardId(Long boardId);
}