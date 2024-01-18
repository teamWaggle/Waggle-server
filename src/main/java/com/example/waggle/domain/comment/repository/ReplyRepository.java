package com.example.waggle.domain.comment.repository;

import com.example.waggle.domain.comment.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByCommentId(Long commentId);

    Page<Reply> findPagedReplyByCommentId(Long CommentId, Pageable pageable);

    void deleteAllByMemberUsername(String username);
}