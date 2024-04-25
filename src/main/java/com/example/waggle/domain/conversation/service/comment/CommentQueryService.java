package com.example.waggle.domain.conversation.service.comment;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.conversation.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentQueryService {
    List<Comment> getComments(Long boardId);

    Page<Comment> getPagedComments(Long boardId, Pageable pageable);

    Page<Comment> getPagedCommentsByUserUrl(String userUrl, Class<? extends Board> boardType, Pageable pageable);

}
