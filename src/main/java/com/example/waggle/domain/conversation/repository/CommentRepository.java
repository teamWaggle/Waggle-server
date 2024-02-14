package com.example.waggle.domain.conversation.repository;

import com.example.waggle.domain.conversation.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardId(Long boardId);

    Page<Comment> findPagedCommentsByBoardId(Long boardId, Pageable pageable);

    void deleteAllByMemberUsername(String username);

    void deleteAllByBoardId(Long boardId);

    boolean existsByMemberId(Long memberId);
}