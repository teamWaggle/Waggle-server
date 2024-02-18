package com.example.waggle.domain.conversation.repository;

import com.example.waggle.domain.conversation.entity.Comment;
import com.example.waggle.domain.conversation.entity.Reply;
import com.example.waggle.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByCommentId(Long commentId);

    Page<Reply> findPagedReplyByCommentId(Long CommentId, Pageable pageable);

    void deleteAllByMemberUsername(String username);

    void deleteAllByCommentId(Long commentId);

    List<Reply> findByComment(Comment comment);

    List<Reply> findByMember(Member member);

}