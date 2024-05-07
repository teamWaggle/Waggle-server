package com.example.waggle.domain.conversation.persistence.dao.comment.jpa;

import com.example.waggle.domain.board.persistence.entity.Board;
import com.example.waggle.domain.conversation.persistence.entity.Comment;
import com.example.waggle.domain.member.persistence.entity.Member;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardId(Long boardId);

    Page<Comment> findPagedCommentsByBoardId(Long boardId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.member.userUrl = :userUrl AND TYPE(c.board) = :boardType")
    Page<Comment> findPagedCommentsByMemberUserUrl(@Param("userUrl") String userUrl,
                                                   @Param("boardType") Class<? extends Board> boardType,
                                                   Pageable pageable);

    void deleteAllByMemberUsername(String username);

    void deleteAllByBoardId(Long boardId);

    boolean existsByMemberId(Long memberId);

    List<Comment> findByBoard(Board board);

    List<Comment> findByMember(Member member);

}