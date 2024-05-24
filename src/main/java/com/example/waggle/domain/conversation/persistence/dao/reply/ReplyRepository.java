package com.example.waggle.domain.conversation.persistence.dao.reply;

import com.example.waggle.domain.conversation.persistence.entity.Comment;
import com.example.waggle.domain.conversation.persistence.entity.Reply;
import com.example.waggle.domain.member.persistence.entity.Member;
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

    void deleteAllByMemberId(Long memberId);
}