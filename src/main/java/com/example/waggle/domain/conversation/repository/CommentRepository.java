package com.example.waggle.domain.conversation.repository;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.conversation.entity.Comment;
import com.example.waggle.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardId(Long boardId);

    Page<Comment> findPagedCommentsByBoardId(Long boardId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.member.userUrl = :userUrl AND TYPE(c.board) = :boardType")
    Page<Comment> findPagedCommentsByMemberUserUrl(String userUrl, Class<? extends Board> boardType, Pageable pageable);

    void deleteAllByMemberUsername(String username);

    void deleteAllByBoardId(Long boardId);

    boolean existsByMemberId(Long memberId);

    List<Comment> findByBoard(Board board);

    List<Comment> findByMember(Member member);

}