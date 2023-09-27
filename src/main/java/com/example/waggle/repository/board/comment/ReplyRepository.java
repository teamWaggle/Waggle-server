package com.example.waggle.repository.board.comment;

import com.example.waggle.domain.board.comment.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
//    @Query(value = "SELECT IFNULL(MAX(r.orders), 0) FROM reply AS r" +
//            " WHERE r.comment_id = :commentId",nativeQuery = true)
//    int findLastOrderByCommentId(@Param("commentId") Long Id);

    List<Reply> findByCommentId(Long commentId);
}