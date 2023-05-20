package com.example.waggle.repository.board.comment;

import com.example.waggle.domain.board.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

//    @Query("select * from comment where c.board.id = :boardId")
//    List<Comment> findByBoardId(Long id);
}