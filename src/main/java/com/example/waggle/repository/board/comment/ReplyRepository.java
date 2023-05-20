package com.example.waggle.repository.board.comment;

import com.example.waggle.domain.board.comment.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    //@Query("select r from reply where r.comment.id = :commentId")
    List<Reply> findByCommentId(@Param("commentId") Long id);
}